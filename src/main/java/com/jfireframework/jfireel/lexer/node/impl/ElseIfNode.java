package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.Lexer;
import com.jfireframework.jfireel.lexer.node.AssociationNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;
import com.jfireframework.jfireel.syntax.Syntax;

public class ElseIfNode implements AssociationNode
{
	private Lexer	conditionLexer;
	private Syntax	associationSyntax;
	
	public ElseIfNode(Lexer conditionLexer)
	{
		this.conditionLexer = conditionLexer;
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		throw new UnsupportedOperationException("该节点不参与计算");
	}
	
	@Override
	public CalculateType type()
	{
		return Expression.ELSE_IF;
	}
	
	@Override
	public void setAssociation(Syntax syntax)
	{
		associationSyntax = syntax;
	}
	
	public Lexer getConditionLexer()
	{
		return conditionLexer;
	}
	
	public Syntax getAssociationSyntax()
	{
		return associationSyntax;
	}
	
	@Override
	public void check()
	{
		if (associationSyntax == null)
		{
			throw new IllegalArgumentException("else 节点的方法体不存在");
		}
	}
	
}
