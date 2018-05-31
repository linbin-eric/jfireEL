package com.jfireframework.jfireel.lexer.node.impl;

import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;
import com.jfireframework.jfireel.lexer.token.Operator;

public abstract class OperatorResultNode implements CalculateNode
{
	protected CalculateNode	leftOperand;
	protected CalculateNode	rightOperand;
	protected Operator		type;
	
	protected OperatorResultNode(Operator type)
	{
		this.type = type;
	}
	
	public void setLeftOperand(CalculateNode node)
	{
		leftOperand = node;
	}
	
	public void setRightOperand(CalculateNode node)
	{
		rightOperand = node;
	}
	
	@Override
	public CalculateType type()
	{
		return Expression.OPERATOR_RESULT;
	}
	
	@Override
	public String toString()
	{
		return "OperatorResultNode [leftOperand=" + leftOperand + ", rightOperand=" + rightOperand + ", type=" + type + "]";
	}
	
}