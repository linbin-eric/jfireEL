package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.token.CalculateType;
import com.jfireframework.jfireel.token.Operator;

public class OperatorNode implements CalculateNode
{
	private Operator operatorType;
	
	public OperatorNode(Operator operatorType)
	{
		this.operatorType = operatorType;
	}
	
	// 操作符节点不会有计算动作
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public CalculateType type()
	{
		return operatorType;
	}
	
}
