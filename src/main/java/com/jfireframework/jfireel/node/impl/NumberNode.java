package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.token.CalculateType;
import com.jfireframework.jfireel.token.Expression;

public class NumberNode implements CalculateNode
{
	private Number value;
	
	public NumberNode(String literals)
	{
		if (literals.indexOf('.') > -1)
		{
			value = Double.valueOf(literals);
		}
		else
		{
			value = Long.valueOf(literals);
		}
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		return value;
	}
	
	@Override
	public CalculateType type()
	{
		return Expression.NUMBER;
	}

	@Override
	public String toString()
	{
		return "NumberNode [value=" + value + "]";
	}
	
}