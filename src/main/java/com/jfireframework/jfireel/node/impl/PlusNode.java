package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.token.Operator;
import com.jfireframework.jfireel.util.number.PlusUtil;

public class PlusNode extends OperatorResultNode
{
    
    public PlusNode()
    {
        super(Operator.PLUS);
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
        if (leftValue instanceof String || rightValue instanceof String)
        {
            StringBuilder builder = new StringBuilder();
            builder.append(leftValue).append(rightValue);
            return builder.toString();
        }
        return PlusUtil.plus((Number) leftValue, (Number) rightValue);
    }
    
}
