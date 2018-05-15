package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.token.Operator;
import com.jfireframework.jfireel.util.number.MutliUtil;

public class MutliNode extends OperatorResultNode
{
    
    public MutliNode()
    {
        super(Operator.MULTI);
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
        return MutliUtil.mutli((Number) leftValue, (Number) rightValue);
    }
    
}
