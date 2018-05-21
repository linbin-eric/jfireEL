package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.token.CalculateType;
import com.jfireframework.jfireel.token.Expression;

public class TypeNode implements CalculateNode
{
	private Class<?> type;
	
	public TypeNode(Class<?> type)
	{
		this.type = type;
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		return type;
	}
	
	@Override
	public CalculateType type()
	{
		return Expression.TYPE;
	}
	
}
