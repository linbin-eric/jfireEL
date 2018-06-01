package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.TokenType;
import com.jfireframework.jfireel.lexer.token.Token;

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
	public TokenType type()
	{
		return Token.STRING;
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
