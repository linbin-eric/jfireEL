package com.jfireframework.jfireel.lexer.node.impl;

import java.util.List;
import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;

public class BracketNode implements CalculateNode
{
	private CalculateNode	beanNode;
	private CalculateNode	valueNode;
	private ValueType		type;
	
	enum ValueType
	{
		STRING, NUMBER, RUNTIME
	}
	
	/**
	 * 代表[]的节点
	 * 
	 * @param beanNode 元素节点
	 * @param valueNode [] 内置的节点
	 */
	public BracketNode(CalculateNode beanNode, CalculateNode valueNode)
	{
		this.beanNode = beanNode;
		this.valueNode = valueNode;
		if (valueNode.type() == Expression.STRING)
		{
			type = ValueType.STRING;
		}
		else if (valueNode.type() == Expression.NUMBER)
		{
			type = ValueType.NUMBER;
		}
		else
		{
			type = ValueType.RUNTIME;
		}
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		Object value = valueNode.calculate(variables);
		if (value == null)
		{
			return null;
		}
		switch (type)
		{
			case STRING:
				return returnMapValue(variables, (String) value);
			case NUMBER:
				return returnArrayOrListValue(variables, (Number) value);
			case RUNTIME:
				if (value instanceof String)
				{
					return returnMapValue(variables, (String) value);
				}
				else if (value instanceof Number)
				{
					return returnArrayOrListValue(variables, (Number) value);
				}
				else
				{
					throw new IllegalArgumentException(value.getClass().getName());
				}
			default:
				throw new UnsupportedOperationException();
		}
	}
	
	private Object returnArrayOrListValue(Map<String, Object> variables, Number value)
	{
		Object beanValue = beanNode.calculate(variables);
		if (beanValue == null)
		{
			return null;
		}
		if (beanValue instanceof List)
		{
			return ((List<?>) beanValue).get((value).intValue());
		}
		else if (beanValue instanceof Object[])
		{
			return ((Object[]) beanValue)[(value).intValue()];
		}
		else if (beanValue instanceof int[])
		{
			return ((int[]) beanValue)[(value).intValue()];
		}
		else if (beanValue instanceof long[])
		{
			return ((long[]) beanValue)[(value).intValue()];
		}
		else if (beanValue instanceof short[])
		{
			return ((short[]) beanValue)[(value).intValue()];
		}
		else if (beanValue instanceof float[])
		{
			return ((float[]) beanValue)[(value).intValue()];
		}
		else if (beanValue instanceof double[])
		{
			return ((double[]) beanValue)[(value).intValue()];
		}
		else if (beanValue instanceof boolean[])
		{
			return ((boolean[]) beanValue)[(value).intValue()];
		}
		else if (beanValue instanceof char[])
		{
			return ((char[]) beanValue)[(value).intValue()];
		}
		else if (beanValue instanceof byte[])
		{
			return ((byte[]) beanValue)[(value).intValue()];
		}
		else
		{
			throw new IllegalArgumentException(beanValue.getClass().getName());
		}
	}
	
	@SuppressWarnings("unchecked")
	private Object returnMapValue(Map<String, Object> variables, String value)
	{
		return ((Map<String, Object>) beanNode.calculate(variables)).get(value);
	}
	
	@Override
	public CalculateType type()
	{
		return Expression.BRACKET;
	}

	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
	
}
