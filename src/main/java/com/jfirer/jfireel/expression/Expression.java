package com.jfirer.jfireel.expression;

import com.jfirer.baseutil.reflect.valueaccessor.ValueAccessor;
import com.jfirer.jfireel.expression.format.FormatContext;
import com.jfirer.jfireel.expression.format.FormatToken;
import com.jfirer.jfireel.expression.impl.operand.method.MethodInvoker;

import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Expression
{
    public static final Matrix                         MATRIX                    = new Matrix("default", null);
    public static final Map<String, Matrix>            NAME_SPACE                = new ConcurrentHashMap<>();
    public static final Map<Field, ValueAccessor>      SHARE_VALUEACCESSOR_CACHE = new ConcurrentHashMap<>();
    public static final Map<Executable, MethodInvoker> SHARE_METHODINVOKER       = new ConcurrentHashMap<>();

    static
    {
        NAME_SPACE.put(MATRIX.getName(), MATRIX);
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

    public static void registerInnerCall(String name, MethodInvoker function)
    {
        MATRIX.registerInnerCall(name, function);
    }

    public static void registerInnerCall(String name, MethodInvoker function, String matrixName)
    {
        Matrix matrix = NAME_SPACE.computeIfAbsent(matrixName, k -> new Matrix(k, MATRIX));
        matrix.registerInnerCall(name, function);
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
