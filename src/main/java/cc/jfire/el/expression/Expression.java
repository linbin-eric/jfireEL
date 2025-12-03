package cc.jfire.el.expression;

import cc.jfire.baseutil.StringUtil;
import cc.jfire.baseutil.reflect.valueaccessor.ValueAccessor;
import cc.jfire.el.ReferenceCall;
import cc.jfire.el.expression.format.FormatContext;
import cc.jfire.el.expression.format.FormatToken;
import cc.jfire.el.expression.impl.operand.method.MethodInvoker;
import lombok.SneakyThrows;

import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Expression
{
    public static final  Matrix                         MATRIX                    = new Matrix("default", null);
    public static final  Map<String, Matrix>            NAME_SPACE                = new ConcurrentHashMap<>();
    public static final  Map<Field, ValueAccessor>      SHARE_VALUEACCESSOR_CACHE = new ConcurrentHashMap<>();
    public static final  Map<Executable, MethodInvoker> SHARE_METHODINVOKER       = new ConcurrentHashMap<>();
    private static final Set<Class>                     SCANED                    = new HashSet<>();

    static
    {
        NAME_SPACE.put(MATRIX.getName(), MATRIX);
        MATRIX.registerClassName(String.class.getSimpleName(), String.class);
        MATRIX.registerClassName(Integer.class.getSimpleName(), Integer.class);
        MATRIX.registerClassName(Long.class.getSimpleName(), Long.class);
        MATRIX.registerClassName(Float.class.getSimpleName(), Float.class);
        MATRIX.registerClassName(Double.class.getSimpleName(), Double.class);
        MATRIX.registerClassName(Boolean.class.getSimpleName(), Boolean.class);
        MATRIX.registerClassName(Character.class.getSimpleName(), Character.class);
        MATRIX.registerClassName(Byte.class.getSimpleName(), Byte.class);
        MATRIX.registerClassName(Short.class.getSimpleName(), Short.class);
        MATRIX.registerClassName("int", int.class);
        MATRIX.registerClassName("byte", byte.class);
        MATRIX.registerClassName("long", long.class);
        MATRIX.registerClassName("float", float.class);
        MATRIX.registerClassName("double", double.class);
        MATRIX.registerClassName("boolean", boolean.class);
        MATRIX.registerClassName("char", char.class);
        MATRIX.registerClassName("short", short.class);
        MATRIX.registerClassName(HashMap.class.getSimpleName(), HashMap.class);
        MATRIX.registerClassName(HashSet.class.getSimpleName(), HashSet.class);
        MATRIX.registerClassName(List.class.getSimpleName(), List.class);
        MATRIX.registerClassName(Map.class.getSimpleName(), Map.class);
        MATRIX.registerClassName(Set.class.getSimpleName(), Set.class);
        MATRIX.registerClassName(StringBuilder.class.getSimpleName(), StringBuilder.class);
        MATRIX.registerClassName(BigDecimal.class.getSimpleName(), BigDecimal.class);
    }

    public static void registerClass(String name, Class<?> ckass)
    {
        MATRIX.registerClassName(name, ckass);
    }

    public static void registerClass(String className, Class ckass, String matrixName)
    {
        Matrix matrix = NAME_SPACE.computeIfAbsent(matrixName, k -> new Matrix(k, MATRIX));
        matrix.registerClassName(className, ckass);
    }

    public static void registerAcceleratorForPropertyRead(Field field, Function<Object, Object> accelerator)
    {
        MATRIX.registerAcceleratorForPropertyRead(field, accelerator);
    }

    public static void registerAcceleratorForPropertyRead(Field field, Function<Object, Object> accelerator, String matrixName)
    {
        Matrix matrix = NAME_SPACE.computeIfAbsent(matrixName, k -> new Matrix(k, MATRIX));
        matrix.registerAcceleratorForPropertyRead(field, accelerator);
    }

    public static void registerAcceleratorForMethodInvoke(Executable executable, MethodInvoker methodInvoker)
    {
        MATRIX.registerAcceleratorForMethodInvoke(executable, methodInvoker);
    }

    public static void registerAcceleratorForMethodInvoke(Executable executable, MethodInvoker methodInvoker, String matrixName)
    {
        Matrix matrix = NAME_SPACE.computeIfAbsent(matrixName, k -> new Matrix(k, MATRIX));
        matrix.registerAcceleratorForMethodInvoke(executable, methodInvoker);
    }

    public static void registerFunctionCall(String content)
    {
        MATRIX.registerFunctionCall(content);
    }

    public static void registerFunctionCall(String content, String matrixName)
    {
        Matrix matrix = NAME_SPACE.computeIfAbsent(matrixName, k -> new Matrix(k, MATRIX));
        matrix.registerFunctionCall(content);
    }

    public static Operand parse(String el)
    {
        return parse(el, MATRIX);
    }

    public static Operand parse(String el, Matrix matrix)
    {
        return new ParseContext(el, matrix).parse();
    }

    public static Operand parse(String el, String matrixName)
    {
        Matrix matrix = NAME_SPACE.get(matrixName);
        if (matrix == null)
        {
            return parse(el);
        }
        else
        {
            return parse(el, matrix);
        }
    }

    public static Operand parse(String el, Matrix matrix, ELConfig config)
    {
        return new ParseContext(el, matrix, config).parse();
    }

    public static Operand parse(String el, String matrixName, ELConfig config)
    {
        Matrix matrix = NAME_SPACE.get(matrixName);
        if (matrix == null)
        {
            return parse(el, config);
        }
        else
        {
            return parse(el, matrix, config);
        }
    }

    @SneakyThrows
    public static void scanForReferenceCall(Class ckass)
    {
        if (SCANED.add(ckass) == false)
        {
            return;
        }
        for (Method method : ckass.getMethods())
        {
            if (method.isAnnotationPresent(ReferenceCall.class))
            {
                ReferenceCall referenceCall = method.getAnnotation(ReferenceCall.class);
                String        callName      = StringUtil.isNotBlank(referenceCall.value()) ? referenceCall.value() : method.getName();
                if (StringUtil.isNotBlank(referenceCall.matrixName()))
                {
                    registerReferenceCall(callName, method, referenceCall.matrixName());
                }
                else
                {
                    registerReferenceCall(callName, method);
                }
            }
        }
    }

    public static void registerReferenceCall(String name, Method method)
    {
        MATRIX.registerReferenceCall(name, method);
    }

    public static void registerReferenceCall(String name, Method method, String matrixName)
    {
        Matrix matrix = NAME_SPACE.computeIfAbsent(matrixName, k -> new Matrix(k, MATRIX));
        matrix.registerReferenceCall(name, method);
    }

    public static Operand parse(String el, ELConfig config)
    {
        return parse(el, MATRIX, config);
    }

    /**
     * 对表达式内容进行格式化并且返回。
     * 格式化的要点有：
     * 1. 遇到{进行换行，该符号独占一行。并且下一行对比该行缩进 4 个空格。
     * 2. 遇到;进行换行。
     * 3. 遇到}进行换行，该符号独占一行。并且下一行对比改行取消缩进 4 个空格。
     *
     * @param content
     * @return
     */
    public static String format(String content)
    {
        ParseContext parseContext = new ParseContext(content, MATRIX);
        parseContext.parse();
        List<FormatToken> formatTokens = parseContext.getFormatTokens();
        StringBuilder     builder      = new StringBuilder();
        FormatContext     context      = new FormatContext();
        formatTokens.forEach(token -> token.out(builder, context));
        return builder.toString().trim();
    }
}
