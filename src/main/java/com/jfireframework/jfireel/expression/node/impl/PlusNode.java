package com.jfireframework.jfireel.expression.node.impl;

import com.jfireframework.jfireel.expression.token.Operator;
import com.jfireframework.jfireel.expression.util.number.PlusUtil;

import java.util.Map;

public class PlusNode extends OperatorResultNode
{
    private static final ThreadLocal<StringBuilder> LOCAL = new ThreadLocal<StringBuilder>()
    {
        protected StringBuilder initialValue()
        {
            return new StringBuilder();
        }

        ;
    };

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
            StringBuilder cache = LOCAL.get();
            cache.append(leftValue).append(rightValue);
            String result = cache.toString();
            cache.setLength(0);
            return result;
        }
        return PlusUtil.calculate((Number) leftValue, (Number) rightValue);
    }

    @Override
    public void check()
    {
        // TODO Auto-generated method stub
    }
}
