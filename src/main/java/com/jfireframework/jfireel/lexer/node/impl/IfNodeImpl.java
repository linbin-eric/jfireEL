package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.Lexer;
import com.jfireframework.jfireel.lexer.node.ElseNode;
import com.jfireframework.jfireel.lexer.node.IfNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;
import com.jfireframework.jfireel.syntax.Syntax;

public class IfNodeImpl implements IfNode
{
	private Lexer		conditionLexer;
	private Syntax		associationSynTax;
	private ElseNode	elseNode;
	
	public IfNodeImpl(Lexer conditionLexer)
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
	
	@Override
	public void setElseNode(ElseNode elseNode)
	{
		this.elseNode = elseNode;
	}
	
}
