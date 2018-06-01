package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.TokenType;
import com.jfireframework.jfireel.lexer.token.Operator;

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
	public TokenType type()
	{
		return operatorType;
	}

	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
	
}
