package com.jfireframework.jfireel.expression.util;

public class Functions
{
    public static final int PROPERTY_FETCH_BY_REFLECT = 1 << 0;
    public static final int PROPERTY_FETCH_BY_UNSAFE  = 1 << 1;
    public static final int PROPERTY_FETCH_BY_METHOD  = 1 << 2;
    public static final int METHOD_INVOKE_BY_REFLECT  = 1 << 3;
    public static final int METHOD_INVOKE_BY_COMPILE  = 1 << 4;
    public static final int RECOGNIZE_EVERY_TIME      = 1 << 5;
    
    public static boolean isMethodInvokeByCompile(int function)
    {
        return (function & METHOD_INVOKE_BY_COMPILE) != 0;
    }
    
    public static boolean isPropertyFetchByUnsafe(int function)
    {
        return (function & PROPERTY_FETCH_BY_UNSAFE) != 0;
    }
    
    public static boolean isPropertyFetchByReflect(int function)
    {
        return (function & PROPERTY_FETCH_BY_REFLECT) != 0;
    }
    
    public static boolean isRecognizeEveryTime(int function)
    {
        return (function & RECOGNIZE_EVERY_TIME) != 0;
    }
}
