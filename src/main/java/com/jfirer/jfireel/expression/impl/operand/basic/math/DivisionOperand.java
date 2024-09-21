package com.jfirer.jfireel.expression.impl.operand.basic.math;

import com.jfirer.jfireel.expression.Operand;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DivisionOperand extends MathOperand
{
    public DivisionOperand(Operand left, Operand right, String fragment)
    {
        super(left, right, fragment);
    }

    @Override
    protected Object mathBigDecimal(Object leftValue, Object rightValue)
    {
        return new BigDecimal(leftValue.toString()).divide(new BigDecimal(rightValue.toString()), 2, RoundingMode.HALF_UP);
    }

    @Override
    protected Number math(int a, int b)
    {
        return a / b;
    }

    @Override
    protected Number math(int a, long b)
    {
        return a / b;
    }

    @Override
    protected Number math(int a, float b)
    {
        return a / b;
    }

    @Override
    protected Number math(int a, double b)
    {
        return a / b;
    }

    @Override
    protected Number math(long a, int b)
    {
        return a / b;
    }

    @Override
    protected Number math(long a, long b)
    {
        return a / b;
    }

    @Override
    protected Number math(long a, float b)
    {
        return a / b;
    }

    @Override
    protected Number math(long a, double b)
    {
        return a / b;
    }

    @Override
    protected Number math(float a, int b)
    {
        return a / b;
    }

    @Override
    protected Number math(float a, long b)
    {
        return a / b;
    }

    @Override
    protected Number math(float a, float b)
    {
        return a / b;
    }

    @Override
    protected Number math(float a, double b)
    {
        return a / b;
    }

    @Override
    protected Number math(double a, int b)
    {
        return a / b;
    }

    @Override
    protected Number math(double a, long b)
    {
        return a / b;
    }

    @Override
    protected Number math(double a, float b)
    {
        return a / b;
    }

    @Override
    protected Number math(double a, double b)
    {
        return a / b;
    }

    @Override
    protected Object processOther(Object leftValue, Object rightValue)
    {
        throw new IllegalStateException("操作数解析出现异常，/ 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
    }
}
