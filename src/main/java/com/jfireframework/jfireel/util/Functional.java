package com.jfireframework.jfireel.util;

public final class Functional
{
    private int function = 0;
    
    public static Functional build()
    {
        return new Functional();
    }
    
    public Functional setMethodInvokeByCompile(boolean flag)
    {
        if (flag)
        {
            function |= Functions.METHOD_INVOKE_BY_COMPILE;
        }
        else
        {
            function &= ~Functions.METHOD_INVOKE_BY_COMPILE;
        }
        return this;
    }
    
    public int toFunction()
    {
        return function;
    }
}
