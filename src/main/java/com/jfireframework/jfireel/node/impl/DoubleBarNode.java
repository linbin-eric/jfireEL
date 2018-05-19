package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.token.Operator;

public class DoubleBarNode extends OperatorResultNode
{
    
    public DoubleBarNode()
    {
        super(Operator.DOUBLE_BAR);
    }
    
    @Override
    public Object calculate(Map<String, Object> variables)
    {
        Object leftValue = leftOperand.calculate(variables);
        if (leftValue == null)
        {
            return null;
        }
        if (((Boolean) leftValue))
        {
            return true;
        }
        Object rightValue = rightOperand.calculate(variables);
        if (rightValue == null)
        {
            return null;
        }
        return (rightValue);
    }
    
}
