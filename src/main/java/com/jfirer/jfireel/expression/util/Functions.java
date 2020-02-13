package com.jfirer.jfireel.expression.util;

public class Functions
{
    public static final int METHOD_INVOKE_BY_REFLECT = 1 << 0;
    public static final int METHOD_INVOKE_BY_COMPILE = 1 << 1;
    public static final int RECOGNIZE_EVERY_TIME     = 1 << 2;

    public static boolean isMethodInvokeByCompile(int function)
    {
        return (function & METHOD_INVOKE_BY_COMPILE) != 0;
    }

    public static boolean isRecognizeEveryTime(int function)
    {
        return (function & RECOGNIZE_EVERY_TIME) != 0;
    }
}
