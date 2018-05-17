package com.jfireframework.jfireel.token;

import java.util.HashMap;
import java.util.Map;

public enum Symbol implements CalculateType
{
	LEFT_PAREN("("), //
	RIGHT_PAREN(")"), //
	LEFT_BRACKET("["), //
	RIGHT_BRACKET("]"), //
	COMMA(",")//
	;
	
	private static Map<String, Symbol> symbols = new HashMap<String, Symbol>(128);
	
	static
	{
		for (Symbol each : symbols.values())
		{
			symbols.put(each.getLiterals(), each);
		}
	}
	
	private Symbol(String literals)
	{
		this.literals = literals;
	}
	
	private final String literals;
	
	/**
	 * 通过字面量查找词法符号.
	 * 
	 * @param literals 字面量
	 * @return 词法符号
	 */
	public static Symbol literalsOf(final String literals)
	{
		return symbols.get(literals);
	}
	
	private String getLiterals()
	{
		return literals;
	}
}
