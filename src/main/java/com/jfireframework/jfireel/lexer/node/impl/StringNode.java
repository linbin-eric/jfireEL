package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;

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

	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
	
}
