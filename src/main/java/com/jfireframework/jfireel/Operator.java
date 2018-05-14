package com.jfireframework.jfireel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum Operator implements CalculateType
{
	PLUS("+"), //
	SUB("-"), //
	MULTI("*"), //
	DIVISION("/"), //
	QUESTION("?"), //
	EQ("=="), //
	GT(">"), //
	LT("<"), //
	PERCENT("%"), //
	COLON(":"), //
	LT_EQ("<="), //
	GT_EQ(">="), //
	BANG_EQ("!="), //
	DOUBLE_AMP("&&"), //
	DOUBLE_BAR("||"), //
	;
	private static Map<String, Operator>	symbols	= new HashMap<String, Operator>(128);
	private static Set<Operator>			store	= new HashSet<Operator>();
	static
	{
		for (Operator each : Operator.values())
		{
			symbols.put(each.getLiterals(), each);
			store.add(each);
		}
	}
	
	private Operator(String literals)
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
	public static Operator literalsOf(final String literals)
	{
		return symbols.get(literals);
	}
	
	public static boolean isOperator(CalculateType type)
	{
		return store.contains(type);
	}
	
	private String getLiterals()
	{
		return literals;
	}
}
