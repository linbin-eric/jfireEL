package com.jfireframework.jfireel.node;

import com.jfireframework.jfireel.CalculateType;
import com.jfireframework.jfireel.Expression;
import com.jfireframework.jfireel.Operator;

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
