package com.jfireframework.jfireel.lexer.node.impl;

import java.lang.reflect.Field;
import java.util.Map;
import com.jfireframework.baseutil.StringUtil;
import com.jfireframework.baseutil.reflect.ReflectUtil;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;
import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class DynamicUnsafePropertyNode implements CalculateNode
{
	private static final Unsafe	unsafe				= ReflectUtil.getUnsafe();
	protected Class<?>			beanType;
	protected FieldType			fieldType;
	protected volatile long		address				= ADDRESS_NOT_INIT;
	protected String			propertyName;
	protected boolean			recognizeEveryTime	= true;
	private CalculateNode		beanNode;
	private static final int	ADDRESS_NOT_INIT	= -1;
	
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
					return unsafe.getInt(value, address);
				case LONG:
					return unsafe.getLong(value, address);
				case SHORT:
					return unsafe.getShort(value, address);
				case FLOAT:
					return unsafe.getFloat(value, address);
				case DOUBLE:
					return unsafe.getDouble(value, address);
				case BYTE:
					return unsafe.getByte(value, address);
				case CHAR:
					return unsafe.getChar(value, address);
				case BOOLEAN:
					return unsafe.getBoolean(value, address);
				case OTHER:
					return unsafe.getObject(value, address);
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
	public CalculateType type()
	{
		return Expression.PROPERTY;
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
								address = unsafe.objectFieldOffset(propertyField);
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
								address = unsafe.objectFieldOffset(propertyField);
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
	public String toString()
	{
		return "PropertyNode [propertyName=" + propertyName + "]";
	}
	
}
