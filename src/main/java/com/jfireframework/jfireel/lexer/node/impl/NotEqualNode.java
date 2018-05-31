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
            else if (leftValue instanceof Boolean && rightValue instanceof Boolean)
            {
                return leftValue.equals(rightValue) == false;
            }
            else
            {
                return EqUtil.calculate((Number) leftValue, (Number) rightValue) == false;
            }
        }
    }

	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
    
}
