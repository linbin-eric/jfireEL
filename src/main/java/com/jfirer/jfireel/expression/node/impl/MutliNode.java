package com.jfirer.jfireel.expression.node.impl;

import java.util.Map;
import com.jfirer.jfireel.expression.token.Operator;
import com.jfirer.jfireel.expression.util.number.MutliUtil;

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
        return MutliUtil.calculate((Number) leftValue, (Number) rightValue);
    }
    
}
