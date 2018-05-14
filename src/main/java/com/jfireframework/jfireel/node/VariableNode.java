package com.jfireframework.jfireel.node;

import java.util.Map;
import com.jfireframework.jfireel.CalculateType;
import com.jfireframework.jfireel.Expression;

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
	public CalculateType type()
	{
		return Expression.VARIABLE;
	}
	
	@Override
	public String toString()
	{
		return "VariableNode [literals=" + literals + "]";
	}
	
}
