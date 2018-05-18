package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.token.Operator;
import com.jfireframework.jfireel.util.number.EqUtil;

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
            else if (leftValue instanceof Boolean && rightValue instanceof Boolean)
            {
                return leftValue.equals(rightValue);
            }
            else
            {
                return EqUtil.calculate((Number) leftValue, (Number) rightValue);
            }
        }
    }
    
}
