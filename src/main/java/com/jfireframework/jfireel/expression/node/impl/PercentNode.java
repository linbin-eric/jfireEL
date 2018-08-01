package com.jfireframework.jfireel.expression.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.expression.token.Operator;
import com.jfireframework.jfireel.expression.util.number.PercentUtil;

public class PercentNode extends OperatorResultNode
{
    public PercentNode()
    {
        super(Operator.PERCENT);
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
        return PercentUtil.calculate((Number) leftValue, (Number) rightValue);
    }
    
    @Override
    public void check()
    {
        // TODO Auto-generated method stub
        
    }
    
}
