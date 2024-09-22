package com.jfirer.jfireel.expression.impl.operand.basic.math.standard;

import com.jfirer.jfireel.expression.Operand;

import java.math.BigDecimal;

public class PlusOperand extends MathOperand
{
    public PlusOperand(String operator, Operand left, Operand right, String fragment)
    {
        super(operator, left, right, fragment);
    }

    @Override
    protected Object mathBigDecimal(Object leftValue, Object rightValue)
    {
        return new BigDecimal(leftValue.toString()).add(new BigDecimal(rightValue.toString()));
    }

    @Override
    protected Number math(int a, int b)
    {
        return a + b;
    }

    @Override
    protected Number math(int a, long b)
    {
        return a + b;
    }

    @Override
    protected Number math(int a, float b)
    {
        return a + b;
    }

    @Override
    protected Number math(int a, double b)
    {
        return a + b;
    }

    @Override
    protected Number math(long a, int b)
    {
        return a + b;
    }

    @Override
    protected Number math(long a, long b)
    {
        return a + b;
    }

    @Override
    protected Number math(long a, float b)
    {
        return a + b;
    }

    @Override
    protected Number math(long a, double b)
    {
        return a + b;
    }

    @Override
    protected Number math(float a, int b)
    {
        return a + b;
    }

    @Override
    protected Number math(float a, long b)
    {
        return a + b;
    }

    @Override
    protected Number math(float a, float b)
    {
        return a + b;
    }

    @Override
    protected Number math(float a, double b)
    {
        return a + b;
    }

    @Override
    protected Number math(double a, int b)
    {
        return a + b;
    }

    @Override
    protected Number math(double a, long b)
    {
        return a + b;
    }

    @Override
    protected Number math(double a, float b)
    {
        return a + b;
    }

    @Override
    protected Number math(double a, double b)
    {
        return a + b;
    }

    @Override
    protected Object processOther(Object leftValue, Object rightValue)
    {
        if (leftValue instanceof String || rightValue instanceof String)
        {
            StringBuilder builder = new StringBuilder();
            return builder.append(leftValue).append(rightValue).toString();
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
}
