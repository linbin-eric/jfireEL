package com.jfirer.jfireel.expression;

import com.jfirer.jfireel.expression.format.FormatContext;
import com.jfirer.jfireel.expression.format.FormatToken;
import com.jfirer.jfireel.expression.impl.operand.MethodInvokeOperand;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Expression
{
    private static Map<String, Class<?>>                                           className                = new ConcurrentHashMap<>();
    private static Map<String, BiFunction<Map<String, Object>, Operand[], Object>> innerCalls               = new ConcurrentHashMap<>();
    private static Map<Method, MethodInvokeOperand.MethodInvokeHelper>             methodInvokeAccelerators = new ConcurrentHashMap<>();
    private static Map<Field, Function<Object, Object>>                            propertyReadAccelerators = new ConcurrentHashMap<>();

    public static void registerClass(String name, Class<?> ckass)
    {
        className.put(name, ckass);
    }

    public static void registerInnerCall(String name, BiFunction<Map<String, Object>, Operand[], Object> function)
    {
        innerCalls.put(name, function);
    }

    public static void registerPropertyReadAccelerator(Field field, Function<Object, Object> accelerator)
    {
        propertyReadAccelerators.put(field, accelerator);
    }

    public static void registerAccelerateInvoker(Method method, MethodInvokeOperand.MethodInvokeHelper methodInvokeHelper)
    {
        methodInvokeAccelerators.put(method, methodInvokeHelper);
    }

    public static Operand parse(String el)
    {
        return new ParseContext(el, className, innerCalls, methodInvokeAccelerators, propertyReadAccelerators).parse();
    }

    public static Operand parseMutli(String el)
    {
        return new ParseContext(el, className, innerCalls, methodInvokeAccelerators, propertyReadAccelerators).parseMutli();
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
        ParseContext parseContext = new ParseContext(content, className, innerCalls, methodInvokeAccelerators, propertyReadAccelerators);
        parseContext.parseMutli();
        List<FormatToken> formatTokens = parseContext.getFormatTokens();
        StringBuilder     builder      = new StringBuilder();
        FormatContext     context      = new FormatContext();
        formatTokens.forEach(token -> token.out(builder, context));
        return builder.toString().trim();
    }
}
