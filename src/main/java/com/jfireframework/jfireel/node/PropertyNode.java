package com.jfireframework.jfireel.node;

import java.lang.reflect.Field;
import com.jfireframework.jfireel.CalculateType;
import com.jfireframework.jfireel.Expression;

public abstract class PropertyNode implements CalculateNode
{
    protected volatile Field field;
    protected String         propertyName;
    
    @Override
    public CalculateType type()
    {
        return Expression.PROPERTY;
    }
    
    protected final Field getField(Object value)
    {
        if (field == null)
        {
            synchronized (this)
            {
                if (field == null)
                {
                    Class<?> ckass = value.getClass();
                    while (ckass != Object.class)
                    {
                        try
                        {
                            field = ckass.getDeclaredField(propertyName);
                            field.setAccessible(true);
                            break;
                        }
                        catch (NoSuchFieldException e)
                        {
                            ckass = ckass.getSuperclass();
                        }
                        catch (SecurityException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                    if (field == null)
                    {
                        throw new NullPointerException();
                    }
                }
            }
        }
        return field;
    }
}
