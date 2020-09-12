package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.token.Operator;
import com.jfirer.jfireel.expression.util.number.GtUtil;

import java.util.Map;

public class GtNode extends OperatorResultNode
{
    public GtNode()
    {
        super(Operator.GT);
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
        return GtUtil.calculate((Number) leftValue, (Number) rightValue);
    }
}
