package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.token.CalculateType;
import com.jfireframework.jfireel.token.Expression;

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
	
}
