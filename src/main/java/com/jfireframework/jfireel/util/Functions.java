package com.jfireframework.jfireel.util;

public class Functions
{
    public static final int PROPERTY_FETCH_BY_REFLECT = 1 << 0;
    public static final int PROPERTY_FETCH_BY_UNSAFE  = 1 << 1;
    public static final int PROPERTY_FETCH_BY_METHOD  = 1 << 2;
    public static final int METHOD_INVOKE_BY_REFLECT  = 1 << 3;
    public static final int METHOD_INVOKE_BY_COMPILE  = 1 << 4;
    
    public static boolean isMethodInvokeByCompile(int function)
    {
        return (function & METHOD_INVOKE_BY_COMPILE) != 0;
    }
}
