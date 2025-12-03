package cc.jfire.el.expression.impl.operand.basic.math.standard;

import cc.jfire.el.expression.Operand;

import java.math.BigDecimal;

public class MinusOperand extends MathOperand
{
    public MinusOperand(String operator, Operand left, Operand right, String fragment)
    {
        super(operator, left, right, fragment);
    }

    @Override
    protected Object mathBigDecimal(Object leftValue, Object rightValue)
    {
        return new BigDecimal(leftValue.toString()).subtract(new BigDecimal(rightValue.toString()));
    }

    @Override
    protected Number math(int a, int b)
    {
        return a - b;
    }

    @Override
    protected Number math(int a, long b)
    {
        return a - b;
    }

    @Override
    protected Number math(int a, float b)
    {
        return a - b;
    }

    @Override
    protected Number math(int a, double b)
    {
        return a - b;
    }

    @Override
    protected Number math(long a, int b)
    {
        return a - b;
    }

    @Override
    protected Number math(long a, long b)
    {
        return a - b;
    }

    @Override
    protected Number math(long a, float b)
    {
        return a - b;
    }

    @Override
    protected Number math(long a, double b)
    {
        return a - b;
    }

    @Override
    protected Number math(float a, int b)
    {
        return a - b;
    }

    @Override
    protected Number math(float a, long b)
    {
        return a - b;
    }

    @Override
    protected Number math(float a, float b)
    {
        return a - b;
    }

    @Override
    protected Number math(float a, double b)
    {
        return a - b;
    }

    @Override
    protected Number math(double a, int b)
    {
        return a - b;
    }

    @Override
    protected Number math(double a, long b)
    {
        return a - b;
    }

    @Override
    protected Number math(double a, float b)
    {
        return a - b;
    }

    @Override
    protected Number math(double a, double b)
    {
        return a - b;
    }

    @Override
    protected Object processOther(Object leftValue, Object rightValue)
    {
        throw new IllegalStateException("操作数解析出现异常，- 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
    }
}
