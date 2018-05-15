package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.token.Operator;

public class MutliNode extends OperatorResultNode
{
	
	public MutliNode()
	{
		super(Operator.MULTI);
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
		if (leftValue instanceof Double || rightValue instanceof Double)
		{
			return ((Number) leftValue).doubleValue() * ((Number) rightValue).doubleValue();
		}
		else
		{
			return ((Number) leftValue).longValue() * ((Number) rightValue).longValue();
		}
	}
	
}
