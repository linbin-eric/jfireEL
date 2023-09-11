package com.jfirer.jfireel.expression;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

public class Expression
{
    private static Map<String, Class<?>>                                           className        = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, List<Method>>                             directMethodName = new ConcurrentHashMap<>();
    private static Map<String, BiFunction<Map<String, Object>, Operand[], Object>> innerCalls       = new ConcurrentHashMap<>();

    public static void registerClass(String name, Class<?> ckass)
    {
        className.put(name, ckass);
    }

    public static void registerInnerCall(String name, BiFunction<Map<String, Object>, Operand[], Object> function)
    {
        innerCalls.put(name, function);
    }

    public static Operand parse(String el)
    {
        return new ParseContext(el, className, innerCalls).parse();
    }

    public static Operand parseMutli(String el)
    {
        return new ParseContext(el, className, innerCalls).parseMutli();
    }
}
