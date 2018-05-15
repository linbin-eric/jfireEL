package com.jfireframework.jfireel.node.impl;

import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.token.CalculateType;
import com.jfireframework.jfireel.token.Expression;
import com.jfireframework.jfireel.token.Operator;

public abstract class OperatorResultNode implements CalculateNode
{
	protected CalculateNode	leftOperand;
	protected CalculateNode	rightOperand;
	protected Operator		type;
	
	protected OperatorResultNode(Operator type)
	{
		this.type = type;
	}
	
	public void addLeftOperand(CalculateNode node)
	{
		leftOperand = node;
	}
	
	public void addRightOperand(CalculateNode node)
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