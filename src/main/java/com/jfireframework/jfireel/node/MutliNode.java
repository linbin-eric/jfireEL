package com.jfireframework.jfireel.node;

import java.util.Map;

public class MutliNode extends OperatorResultNode
{
	
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
