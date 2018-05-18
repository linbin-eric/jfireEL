package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.token.Operator;
import com.jfireframework.jfireel.util.number.DivisionUtil;

public class DivisionNode extends OperatorResultNode
{
    
    public DivisionNode()
    {
        super(Operator.DIVISION);
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
        return DivisionUtil.calculate((Number) leftValue, (Number) rightValue);
    }
    
}
