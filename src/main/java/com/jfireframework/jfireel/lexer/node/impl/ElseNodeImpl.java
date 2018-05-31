package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.node.ElseNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;
import com.jfireframework.jfireel.syntax.Syntax;

public class ElseNodeImpl implements ElseNode
{
	private Syntax associationSynTax;
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		return associationSynTax.calculate(variables);
	}
	
	@Override
	public CalculateType type()
	{
		return Expression.ELSE;
	}
	
	@Override
	public void setAssociation(Syntax syntax)
	{
		associationSynTax = syntax;
	}
}
