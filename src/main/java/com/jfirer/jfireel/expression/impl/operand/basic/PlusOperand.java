package com.jfirer.jfireel.expression.impl.operand.basic;

import com.jfirer.jfireel.expression.Operand;

import java.math.BigDecimal;

public class PlusOperand extends MathOperand
{
    private static final ThreadLocal<StringBuilder> BUILDER = ThreadLocal.withInitial(StringBuilder::new);

    public PlusOperand(Operand left, Operand right, String fragment)
    {
        super(left, right, fragment);
    }

    @Override
    protected Object mathBigDecimal(Object leftValue, Object rightValue)
    {
        return new BigDecimal(leftValue.toString()).add(new BigDecimal(rightValue.toString()));
    }

    @Override
    protected Object math(double v, double v1)
    {
        return v + v1;
    }

    @Override
    protected Object math(double v, long l)
    {
        return v + l;
    }

    @Override
    protected Object math(long l, double v)
    {
        return l + v;
    }

    @Override
    protected Object math(long l, long l1)
    {
        return l + l1;
    }

    @Override
    protected Object processOther(Object leftValue, Object rightValue)
    {
        if (leftValue instanceof String || rightValue instanceof String)
        {
            StringBuilder builder = BUILDER.get();
            String        result  = builder.append(leftValue).append(rightValue).toString();
            builder.setLength(0);
            return result;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
}
