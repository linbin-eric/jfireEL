package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.token.Operator;
import com.jfirer.jfireel.expression.util.number.DivisionUtil;

import java.util.Map;

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

    @Override
    public String literals()
    {
        return "/";
    }

    @Override
    public String toString()
    {
        return literals();
    }
}
