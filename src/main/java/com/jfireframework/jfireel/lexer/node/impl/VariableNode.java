package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.Token;
import com.jfireframework.jfireel.lexer.token.TokenType;

public class VariableNode implements CalculateNode
{
	private String literals;
	
	public VariableNode(String literals)
	{
		this.literals = literals;
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		return variables.get(literals);
	}
	
	@Override
	public TokenType type()
	{
		return Token.VARIABLE;
	}
	
	@Override
	public String toString()
	{
		return "VariableNode [literals=" + literals + "]";
	}
	
	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String literals()
	{
		return literals;
	}
	
}
