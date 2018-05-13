package com.jfireframework.jfireel.node;

import java.lang.reflect.Field;
import com.jfireframework.jfireel.CalculateType;
import com.jfireframework.jfireel.Expression;

public abstract class PropertyNode implements CalculateNode
{
    protected volatile Class<?> beanType;
    protected volatile Field    field;
    protected String            propertyName;
    protected boolean           recognizeEveryTime = true;
    
    @Override
    public CalculateType type()
    {
        return Expression.PROPERTY;
    }
    
    protected final Field getField(Object value)
    {
        if (recognizeEveryTime)
        {
            if (field == null || beanType.isAssignableFrom(value.getClass()))
            {
                synchronized (this)
                {
                    if (field == null || beanType.isAssignableFrom(value.getClass()))
                    {
                        Class<?> ckass = value.getClass();
                        while (ckass != Object.class)
                        {
                            try
                            {
                                field = ckass.getDeclaredField(propertyName);
                                beanType = value.getClass();
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
                        return field;
                    }
                }
            }
            return field;
        }
        else
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
                        return field;
                    }
                }
            }
            return field;
        }
    }
}
