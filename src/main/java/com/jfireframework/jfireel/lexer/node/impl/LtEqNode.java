package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.token.Operator;
import com.jfireframework.jfireel.lexer.util.number.GtUtil;

public class LtEqNode extends OperatorResultNode
{
	public LtEqNode()
	{
		super(Operator.LT_EQ);
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		Object leftValue = leftOperand.calculate(variables);
		if (leftValue == null)
		{
			return null;
		}
		Object rightValue = rightOperand.calculate(variables);
		if (rightValue == null)
		{
			return null;
		}
		return (Boolean) GtUtil.calculate((Number) leftValue, (Number) rightValue) == false;
	}
	
	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
	
}
