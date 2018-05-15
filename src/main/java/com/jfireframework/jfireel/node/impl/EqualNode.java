package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.token.Operator;

public class EqualNode extends OperatorResultNode
{
	
	public EqualNode()
	{
		super(Operator.EQ);
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
				return true;
			}
			else
			{
				return rightValue == null;
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
				return leftValue.equals(rightValue);
			}
			if (leftValue instanceof Double || rightValue instanceof Double)
			{
				return ((Number) leftValue).doubleValue() == ((Number) rightValue).doubleValue();
			}
			else
			{
				return ((Number) leftValue).longValue() == ((Number) rightValue).longValue();
			}
		}
	}
	
}
