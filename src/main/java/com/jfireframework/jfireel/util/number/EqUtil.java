package com.jfireframework.jfireel.util.number;

public class EqUtil
{
    private static boolean intEq(int arg0, Number b)
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
    
    private static boolean longEq(Long arg0, Number b)
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
    
    private static boolean floatEq(float arg0, Number b)
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
    
    private static boolean doubleEq(double arg0, Number b)
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
    
    public static boolean eq(Number a, Number b)
    {
        if (a instanceof Integer || a instanceof Short || a instanceof Byte)
        {
            int arg0 = a.intValue();
            return intEq(arg0, b);
        }
        else if (a instanceof Long)
        {
            long arg0 = a.longValue();
            return longEq(arg0, b);
        }
        else if (a instanceof Float)
        {
            float arg0 = a.floatValue();
            return floatEq(arg0, b);
        }
        else if (a instanceof Double)
        {
            double arg0 = a.doubleValue();
            return doubleEq(arg0, b);
        }
        else
        {
            throw new UnsupportedOperationException(a.getClass().getName() + "不支持该类型的加操作");
        }
    }
}
