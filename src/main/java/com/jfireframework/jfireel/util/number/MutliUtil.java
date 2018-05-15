package com.jfireframework.jfireel.util.number;

public class MutliUtil
{
    private static Object intMutli(int arg0, Number b)
    {
        if (b instanceof Integer || b instanceof Short || b instanceof Byte)
        {
            return arg0 * b.intValue();
        }
        else if (b instanceof Long)
        {
            return arg0 * b.longValue();
        }
        else if (b instanceof Float)
        {
            return arg0 * b.floatValue();
        }
        else if (b instanceof Double)
        {
            return arg0 * b.doubleValue();
        }
        else
        {
            throw new UnsupportedOperationException(b.getClass().getName() + "不支持该类型的加操作");
        }
    }
    
    private static Object longMutli(Long arg0, Number b)
    {
        if (b instanceof Integer || b instanceof Short || b instanceof Byte)
        {
            return arg0 * b.intValue();
        }
        else if (b instanceof Long)
        {
            return arg0 * b.longValue();
        }
        else if (b instanceof Float)
        {
            return arg0 * b.floatValue();
        }
        else if (b instanceof Double)
        {
            return arg0 * b.doubleValue();
        }
        else
        {
            throw new UnsupportedOperationException(b.getClass().getName() + "不支持该类型的加操作");
        }
    }
    
    private static Object floatMutli(float arg0, Number b)
    {
        if (b instanceof Integer || b instanceof Short || b instanceof Byte)
        {
            return arg0 * b.intValue();
        }
        else if (b instanceof Long)
        {
            return arg0 * b.longValue();
        }
        else if (b instanceof Float)
        {
            return arg0 * b.floatValue();
        }
        else if (b instanceof Double)
        {
            return arg0 * b.doubleValue();
        }
        else
        {
            throw new UnsupportedOperationException(b.getClass().getName() + "不支持该类型的加操作");
        }
    }
    
    private static Object doubleMutli(double arg0, Number b)
    {
        if (b instanceof Integer || b instanceof Short || b instanceof Byte)
        {
            return arg0 * b.intValue();
        }
        else if (b instanceof Long)
        {
            return arg0 * b.longValue();
        }
        else if (b instanceof Float)
        {
            return arg0 * b.floatValue();
        }
        else if (b instanceof Double)
        {
            return arg0 * b.doubleValue();
        }
        else
        {
            throw new UnsupportedOperationException(b.getClass().getName() + "不支持该类型的加操作");
        }
    }
    
    public static Object mutli(Number a, Number b)
    {
        if (a instanceof Integer || a instanceof Short || a instanceof Byte)
        {
            int arg0 = a.intValue();
            return intMutli(arg0, b);
        }
        else if (a instanceof Long)
        {
            long arg0 = a.longValue();
            return longMutli(arg0, b);
        }
        else if (a instanceof Float)
        {
            float arg0 = a.floatValue();
            return floatMutli(arg0, b);
        }
        else if (a instanceof Double)
        {
            double arg0 = a.doubleValue();
            return doubleMutli(arg0, b);
        }
        else
        {
            throw new UnsupportedOperationException(a.getClass().getName() + "不支持该类型的加操作");
        }
    }
}
