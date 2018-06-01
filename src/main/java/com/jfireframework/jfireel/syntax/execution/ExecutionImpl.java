package com.jfireframework.jfireel.syntax.execution;

import java.util.Map;
import com.jfireframework.jfireel.lexer.Expression;

public class ExecutionImpl
{
	public static final int	LITERALS	= 1;
	public static final int	LEXER		= 2;
	public static final int	LANGUAGE	= 3;
	int						type;
	String					literals;
	Expression					lexer;
	
	public ExecutionImpl(Expression lexer, int type)
	{
		this.lexer = lexer;
		this.type = type;
	}
	
	public ExecutionImpl(String literals)
	{
		this.literals = literals;
		type = LITERALS;
	}
	
	public Object calculate(Map<String, Object> params)
	{
		if (type == LITERALS)
		{
			return literals;
		}
		else
		{
			return lexer.calculate(params);
		}
	}
	
	public int type()
	{
		return type;
	}
	
	public Expression lexer()
	{
		return lexer;
	}
	
	public void changeTypeToLexer()
	{
		type = LEXER;
	}
}
