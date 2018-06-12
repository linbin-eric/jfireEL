package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.token.Operator;
import com.jfireframework.jfireel.lexer.util.number.EqUtil;

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
		if (leftValue == null && rightValue == null)
		{
			return false;
		}
		else if (leftValue == null && rightValue != null)
		{
			return true;
		}
		else if (leftValue != null && rightValue == null)
		{
			return true;
		}
		else
		{
			if (leftValue instanceof Number && rightValue instanceof Number)
			{
				return EqUtil.calculate((Number) leftValue, (Number) rightValue) == false;
			}
			else
			{
				return leftValue.equals(rightValue) == false;
			}
		}
	}
	
	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
	
}
