package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.token.Operator;
import com.jfireframework.jfireel.lexer.util.number.MinusUtil;

public class MinusNode extends OperatorResultNode
{
	public MinusNode()
	{
		super(Operator.MINUS);
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
		return MinusUtil.calculate((Number) leftValue, (Number) rightValue);
	}

	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
	
}
