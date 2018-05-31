package com.jfireframework.jfireel.lexer.util.number;

public class EqUtil
{
    private static boolean calculate(int arg0, Number b)
    {
        if (b instanceof Integer || b instanceof Short || b instanceof Byte)
        {
            return arg0 == b.intValue();
        }
        else if (b instanceof Long)
        {
            return arg0 == b.longValue();
        }
        else if (b instanceof Float)
        {
            return arg0 == b.floatValue();
        }
        else if (b instanceof Double)
        {
            return arg0 == b.doubleValue();
        }
        else
        {
            throw new UnsupportedOperationException(b.getClass().getName() + "不支持该类型的加操作");
        }
    }
    
    private static boolean calculate(long arg0, Number b)
    {
        if (b instanceof Integer || b instanceof Short || b instanceof Byte)
        {
            return arg0 == b.intValue();
        }
        else if (b instanceof Long)
        {
            return arg0 == b.longValue();
        }
        else if (b instanceof Float)
        {
            return arg0 == b.floatValue();
        }
        else if (b instanceof Double)
        {
            return arg0 == b.doubleValue();
        }
        else
        {
            throw new UnsupportedOperationException(b.getClass().getName() + "不支持该类型的加操作");
        }
    }
    
    private static boolean calculate(float arg0, Number b)
    {
        if (b instanceof Integer || b instanceof Short || b instanceof Byte)
        {
            return arg0 == b.intValue();
        }
        else if (b instanceof Long)
        {
            return arg0 == b.longValue();
        }
        else if (b instanceof Float)
        {
            return arg0 == b.floatValue();
        }
        else if (b instanceof Double)
        {
            return arg0 == b.doubleValue();
        }
        else
        {
            throw new UnsupportedOperationException(b.getClass().getName() + "不支持该类型的加操作");
        }
    }
    
    private static boolean calculate(double arg0, Number b)
    {
        if (b instanceof Integer || b instanceof Short || b instanceof Byte)
        {
            return arg0 == b.intValue();
        }
        else if (b instanceof Long)
        {
            return arg0 == b.longValue();
        }
        else if (b instanceof Float)
        {
            return arg0 == b.floatValue();
        }
        else if (b instanceof Double)
        {
            return arg0 == b.doubleValue();
        }
        else
        {
            throw new UnsupportedOperationException(b.getClass().getName() + "不支持该类型的加操作");
        }
    }
    
    public static boolean calculate(Number a, Number b)
    {
        if (a instanceof Integer || a instanceof Short || a instanceof Byte)
        {
            int arg0 = a.intValue();
            return calculate(arg0, b);
        }
        else if (a instanceof Long)
        {
            long arg0 = a.longValue();
            return calculate(arg0, b);
        }
        else if (a instanceof Float)
        {
            float arg0 = a.floatValue();
            return calculate(arg0, b);
        }
        else if (a instanceof Double)
        {
            double arg0 = a.doubleValue();
            return calculate(arg0, b);
        }
        else
        {
            throw new UnsupportedOperationException(a.getClass().getName() + "不支持该类型的加操作");
        }
    }
}
