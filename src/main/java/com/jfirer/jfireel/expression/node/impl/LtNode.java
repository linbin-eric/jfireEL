package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.token.Operator;
import com.jfirer.jfireel.expression.util.number.LtUtil;

import java.util.Map;

public class LtNode extends OperatorResultNode
{

    public LtNode()
    {
        super(Operator.LT);
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
        return LtUtil.calculate((Number) leftValue, (Number) rightValue);
    }
}
