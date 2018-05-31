package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.token.Operator;

public class DoubleAmpNode extends OperatorResultNode
{
    public DoubleAmpNode()
    {
        super(Operator.DOUBLE_AMP);
    }
    
    @Override
    public Object calculate(Map<String, Object> variables)
    {
        Object leftValue = leftOperand.calculate(variables);
        if (leftValue == null)
        {
            return null;
        }
        if (((Boolean) leftValue) == false)
        {
            return false;
        }
        Object rightValue = rightOperand.calculate(variables);
        if (rightValue == null)
        {
            return null;
        }
        return (rightValue);
    }

	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
    
}
