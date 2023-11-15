package com.jfirer.jfireel.expression;

import com.jfirer.jfireel.expression.impl.operand.MethodInvokeOperand;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        StringBuilder builder = new StringBuilder();
        int           indent  = 0;
        for (char c : content.toCharArray())
        {
            if (CharType.isIgnore(c) == false)
            {
                if (c == ';')
                {
                    builder.append(";\r\n");
                }
                else if (c == '{')
                {
                    if (builder.length() > 0)
                    {
                        char pre = builder.charAt(builder.length() - 1);
                        if (pre == '\r' || pre == '\n')
                        {
                            ;
                        }
                        else{
                            builder.append("\r\n");
                        }
                    }
                    for (int i = 0; i < indent; i++)
                    {
                        builder.append(' ');
                    }
                    builder.append("{\r\n");
                    indent += 4;
                    for (int i = 0; i < indent; i++)
                    {
                        builder.append(' ');
                    }
                }
                else if (c == '}')
                {
                    if (builder.length() > 0)
                    {
                        char pre = builder.charAt(builder.length() - 1);
                        if (pre == '\r' || pre == '\n')
                        {
                            ;
                        }
                        else{
                            builder.append("\r\n");
                        }
                    }
                    indent -= 4;
                    for (int i = 0; i < indent; i++)
                    {
                        builder.append(' ');
                    }
                    builder.append("}\r\n");
                    for (int i = 0; i < indent; i++)
                    {
                        builder.append(' ');
                    }
                }
                else
                {
                    builder.append(c);
                }
            }
        }
        String s = builder.toString();
        if(s.endsWith("\r\n    "))
    }
}
