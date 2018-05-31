package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;

public class TypeNode implements CalculateNode
{
	private Class<?>	ckass;
	private Expression	type;
	
	public TypeNode(Class<?> ckass, Expression type)
	{
		this.ckass = ckass;
		this.type = type;
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		return ckass;
	}
	
	@Override
	public CalculateType type()
	{
		return type;
	}

	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
	
}
