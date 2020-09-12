package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.token.Operator;

import java.util.Map;

import static com.jfirer.jfireel.expression.util.OperatorResultUtil.trueOfFalse;

public class DoubleAmpNode extends OperatorResultNode
{
    public DoubleAmpNode()
    {
        super(Operator.DOUBLE_AMP);
    }

    @Override
    public Object calculate(Map<String, Object> variables)
    {
        return trueOfFalse(leftOperand.calculate(variables)) != false && trueOfFalse(rightOperand.calculate(variables)) != false;
    }

    @Override
    public String toString()
    {
        return literals();
    }
}
