package com.jfireframework.jfireel.node;

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
	
}
