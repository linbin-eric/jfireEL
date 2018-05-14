package com.jfireframework.jfireel.node;

import com.jfireframework.jfireel.CalculateType;
import com.jfireframework.jfireel.Expression;

public abstract class OperatorResultNode implements CalculateNode
{
	protected CalculateNode	leftOperand;
	protected CalculateNode	rightOperand;
	
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
	
}
