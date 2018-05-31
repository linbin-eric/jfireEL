package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.Lexer;
import com.jfireframework.jfireel.Syntax;
import com.jfireframework.jfireel.node.AssociationNode;
import com.jfireframework.jfireel.token.CalculateType;
import com.jfireframework.jfireel.token.Expression;

public class IfNode implements AssociationNode
{
	private Lexer	conditionLexer;
	private Syntax	associationSynTax;
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		if ((Boolean) conditionLexer.calculate(variables))
		{
			return associationSynTax.calculate(variables);
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public CalculateType type()
	{
		return Expression.IF;
	}
	
	@Override
	public void setAssociation(String syntax)
	{
		associationSynTax = Syntax.parse(syntax);
	}
	
}
