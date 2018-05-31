package com.jfireframework.jfireel.syntax.handler;

import java.util.Map;
import com.jfireframework.jfireel.lexer.Lexer;

public class Execution
{
	public static final int	LITERALS	= 1;
	public static final int	LEXER		= 2;
	public static final int	LANGUAGE	= 3;
	int						type;
	String					literals;
	Lexer					lexer;
	
	public Execution(Lexer lexer, int type)
	{
		this.lexer = lexer;
		this.type = type;
	}
	
	public Execution(String literals)
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
	
	public Lexer lexer()
	{
		return lexer;
	}
	
	public void changeTypeToLexer()
	{
		type = LEXER;
	}
}
