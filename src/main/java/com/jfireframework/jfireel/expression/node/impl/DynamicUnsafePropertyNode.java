package com.jfireframework.jfireel.expression.node.impl;

import java.lang.reflect.Field;
import java.util.Map;
import com.jfireframework.baseutil.StringUtil;
import com.jfireframework.baseutil.reflect.UNSAFE;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.token.Token;
import com.jfireframework.jfireel.expression.token.TokenType;

public class DynamicUnsafePropertyNode implements CalculateNode
{
    protected Class<?>       beanType;
    protected FieldType      fieldType;
    protected volatile long  address            = ADDRESS_NOT_INIT;
    protected String         propertyName;
    protected boolean        recognizeEveryTime = true;
    private CalculateNode    beanNode;
    private static final int ADDRESS_NOT_INIT   = -1;
    
    enum FieldType
    {
        INT, SHORT, LONG, FLOAT, DOUBLE, BOOLEAN, BYTE, CHAR, OTHER
    }
    
    /**
     * 使用通过变量名和属性名访问该变量的属性
     * 
     * @param literals
     */
    public DynamicUnsafePropertyNode(String literals, CalculateNode beanNode, boolean recognizeEveryTime)
    {
        propertyName = literals;
        this.beanNode = beanNode;
        this.recognizeEveryTime = recognizeEveryTime;
    }
    
    @Override
    public Object calculate(Map<String, Object> variables)
    {
        Object value = beanNode.calculate(variables);
        if (value == null)
        {
            return null;
        }
        try
        {
            long address = getAddress(value);
            switch (fieldType)
            {
                case INT:
                    return UNSAFE.getInt(value, address);
                case LONG:
                    return UNSAFE.getLong(value, address);
                case SHORT:
                    return UNSAFE.getShort(value, address);
                case FLOAT:
                    return UNSAFE.getFloat(value, address);
                case DOUBLE:
                    return UNSAFE.getDouble(value, address);
                case BYTE:
                    return UNSAFE.getByte(value, address);
                case CHAR:
                    return UNSAFE.getChar(value, address);
                case BOOLEAN:
                    return UNSAFE.getBoolean(value, address);
                case OTHER:
                    return UNSAFE.getObject(value, address);
                default:
                    throw new UnsupportedOperationException();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public TokenType type()
    {
        return Token.PROPERTY;
    }
    
    protected final long getAddress(Object value)
    {
        long address = this.address;
        if (recognizeEveryTime)
        {
            if (address == ADDRESS_NOT_INIT || beanType.isAssignableFrom(value.getClass()))
            {
                synchronized (this)
                {
                    if ((address = this.address) == ADDRESS_NOT_INIT || beanType.isAssignableFrom(value.getClass()))
                    {
                        Field propertyField = null;
                        Class<?> ckass = value.getClass();
                        while (ckass != Object.class)
                        {
                            try
                            {
                                propertyField = ckass.getDeclaredField(propertyName);
                                beanType = value.getClass();
                                Class<?> type = propertyField.getType();
                                if (type.isPrimitive() == false)
                                {
                                    fieldType = FieldType.OTHER;
                                }
                                else
                                {
                                    fieldType = FieldType.valueOf(type.getName().toUpperCase());
                                }
                                address = UNSAFE.objectFieldOffset(propertyField);
                                this.address = address;
                                return address;
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
                        throw new NullPointerException(StringUtil.format("无法在类{}中找到属性:{}", value.getClass(), propertyName));
                    }
                }
            }
            return address;
        }
        else
        {
            if (address == ADDRESS_NOT_INIT)
            {
                synchronized (this)
                {
                    if ((address = this.address) == ADDRESS_NOT_INIT)
                    {
                        Field propertyField = null;
                        Class<?> ckass = value.getClass();
                        while (ckass != Object.class)
                        {
                            try
                            {
                                propertyField = ckass.getDeclaredField(propertyName);
                                beanType = value.getClass();
                                Class<?> type = propertyField.getType();
                                if (type.isPrimitive() == false)
                                {
                                    fieldType = FieldType.OTHER;
                                }
                                else
                                {
                                    fieldType = FieldType.valueOf(type.getName().toUpperCase());
                                }
                                address = UNSAFE.objectFieldOffset(propertyField);
                                this.address = address;
                                return address;
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
                        throw new NullPointerException(StringUtil.format("无法在类{}中找到属性:{}", value.getClass(), propertyName));
                    }
                }
            }
            return address;
        }
    }
    
    @Override
    public void check()
    {
        
    }
    
    @Override
    public String literals()
    {
        return beanNode.literals() + "." + propertyName;
    }
    
    @Override
    public String toString()
    {
        return literals();
    }
}
