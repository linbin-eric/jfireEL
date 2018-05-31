package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;

public class NumberNode implements CalculateNode
{
	private Number value;
	
	public NumberNode(String literals)
	{
		if (literals.indexOf('.') > -1)
		{
			value = Float.valueOf(literals);
			if (Float.isInfinite((Float) value))
			{
				value = Double.valueOf(literals);
			}
		}
		else
		{
			try
			{
				value = Integer.valueOf(literals);
			}
			catch (NumberFormatException e)
			{
				value = Long.valueOf(literals);
			}
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
