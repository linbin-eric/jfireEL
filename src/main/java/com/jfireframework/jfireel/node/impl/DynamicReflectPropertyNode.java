package com.jfireframework.jfireel.node.impl;

import java.lang.reflect.Field;
import java.util.Map;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.token.CalculateType;
import com.jfireframework.jfireel.token.Expression;

public class DynamicReflectPropertyNode implements CalculateNode
{
	protected volatile Class<?>	beanType;
	protected volatile Field	field;
	protected String			propertyName;
	protected boolean			recognizeEveryTime	= true;
	private CalculateNode		beanNode;
	
	/**
	 * 使用通过变量名和属性名访问该变量的属性
	 * 
	 * @param literals
	 */
	public DynamicReflectPropertyNode(String literals, CalculateNode beanNode, boolean recognizeEveryTime)
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
			return getField(value).get(value);
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
	
	protected final Field getField(Object value)
	{
		Field propertyField = field;
		if (recognizeEveryTime)
		{
			if (propertyField == null || beanType.isAssignableFrom(value.getClass()))
			{
				synchronized (this)
				{
					if ((propertyField = field) == null || beanType.isAssignableFrom(value.getClass()))
					{
						propertyField = null;
						Class<?> ckass = value.getClass();
						while (ckass != Object.class)
						{
							try
							{
								propertyField = ckass.getDeclaredField(propertyName);
								propertyField.setAccessible(true);
								beanType = value.getClass();
								field = propertyField;
								return propertyField;
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
						throw new NullPointerException();
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
	
	@Override
	public String toString()
	{
		return "PropertyNode [propertyName=" + propertyName + "]";
	}
	
}
