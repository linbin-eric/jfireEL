package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.token.Operator;

public class NotEqualNode extends OperatorResultNode
{
	
	public NotEqualNode()
	{
		super(Operator.NOT_EQ);
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		Object leftValue = leftOperand.calculate(variables);
		Object rightValue = rightOperand.calculate(variables);
		if (leftValue == null)
		{
			if (rightValue == null)
			{
				return false;
			}
			else
			{
				return rightValue != null;
			}
		}
		else
		{
			if (rightValue == null)
			{
				return false;
			}
			if (leftValue instanceof String && rightValue instanceof String)
			{
				return leftValue.equals(rightValue) == false;
			}
			if (leftValue instanceof Double || rightValue instanceof Double)
			{
				return ((Number) leftValue).doubleValue() != ((Number) rightValue).doubleValue();
			}
			else
			{
				return ((Number) leftValue).longValue() != ((Number) rightValue).longValue();
			}
		}
	}
	
}
