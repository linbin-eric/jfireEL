package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.token.Operator;

import java.util.Map;

import static com.jfirer.jfireel.expression.util.OperatorResultUtil.trueOfFalse;

public class DoubleBarNode extends OperatorResultNode
{

    public DoubleBarNode()
    {
        super(Operator.DOUBLE_BAR);
    }

    @Override
    public Object calculate(Map<String, Object> variables)
    {
        return trueOfFalse(leftOperand.calculate(variables)) || trueOfFalse(rightOperand.calculate(variables));
    }

    @Override
    public String toString()
    {
        return literals();
    }
}
