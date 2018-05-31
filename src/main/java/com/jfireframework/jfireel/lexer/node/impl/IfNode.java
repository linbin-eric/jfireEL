package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.Lexer;
import com.jfireframework.jfireel.lexer.node.AssociationNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;
import com.jfireframework.jfireel.syntax.Syntax;

public class IfNode implements AssociationNode
{
	private Lexer		conditionLexer;
	private Syntax		associationSynTax;
	private ElseNode	elseNode;
	
	public IfNode(Lexer conditionLexer)
	{
		this.conditionLexer = conditionLexer;
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		if ((Boolean) conditionLexer.calculate(variables))
		{
			return associationSynTax.calculate(variables);
		}
		else
		{
			if (elseNode != null)
			{
				return elseNode.calculate(variables);
			}
			else
			{
				return null;
			}
		}
	}
	
	@Override
	public CalculateType type()
	{
		return Expression.IF;
	}
	
	@Override
	public void setAssociation(Syntax syntax)
	{
		associationSynTax = syntax;
	}
	
	public void setElseNode(ElseNode elseNode)
	{
		this.elseNode = elseNode;
	}
	
}
