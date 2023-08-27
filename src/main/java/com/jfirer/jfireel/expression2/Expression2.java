package com.jfirer.jfireel.expression2;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;

public class Expression2
{
    private static Map<String, Class<?>>                                          staticClassName  = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, List<Method>>                            directMethodName = new ConcurrentHashMap<>();
    private static Map<String, BiFunction<Map<String, Object>, Object[], Object>> innerCalls       = new ConcurrentHashMap<>();

    public static void registerClass(String name, Class<?> ckass)
    {
        staticClassName.put(name, ckass);
    }

    public static void registerMethod(String name, Method method)
    {
        List<Method> methods = directMethodName.computeIfAbsent(name, s -> new CopyOnWriteArrayList<>());
        methods.add(method);
    }

    public static void registerInnerCall(String name, BiFunction<Map<String, Object>, Object[], Object> function)
    {
        innerCalls.put(name, function);
    }

    public static void registerMethod(Method method)
    {
        registerMethod(method.getName(), method);
    }

    public static Operand parse(String el)
    {
        return new ParseContext(el, staticClassName, directMethodName, innerCalls).parse();
    }

    public static Operand parseMutli(String el)
    {
        return new ParseContext(el, staticClassName, directMethodName, innerCalls).parseMutli();
    }
}
