package com.jfireframework.jfireel.expression.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.expression.token.Operator;
import com.jfireframework.jfireel.expression.util.number.EqUtil;

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
        if (leftValue == null && rightValue == null)
        {
            return false;
        }
        else if (leftValue == null && rightValue != null)
        {
            return false;
        }
        else if (leftValue != null && rightValue == null)
        {
            return false;
        }
        else
        {
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                return EqUtil.calculate((Number) leftValue, (Number) rightValue);
            }
            else
            {
                return leftValue.equals(rightValue);
            }
        }
    }
    
    @Override
    public void check()
    {
        // TODO Auto-generated method stub
        
    }
    
}
