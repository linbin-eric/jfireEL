package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.TokenType;
import com.jfireframework.jfireel.lexer.token.Token;

public class TypeNode implements CalculateNode
{
	private Class<?>	ckass;
	private Token	type;
	
	public TypeNode(Class<?> ckass, Token type)
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
	public TokenType type()
	{
		return type;
	}

	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
	
}
