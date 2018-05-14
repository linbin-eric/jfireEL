package com.jfireframework.jfireel.node;

import java.util.Map;
import com.jfireframework.jfireel.CalculateType;
import com.jfireframework.jfireel.Expression;

public class StringNode implements CalculateNode
{
	private String literals;
	
	public StringNode(String literals)
	{
		this.literals = literals;
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		return literals;
	}
	
	@Override
	public CalculateType type()
	{
		return Expression.STRING;
	}
	
	@Override
	public String toString()
	{
		return "StringNode [literals=" + literals + "]";
	}
	
}
