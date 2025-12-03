package cc.jfire.el.expression.impl.operand.basic.math.standard;

import cc.jfire.el.expression.Operand;

import java.math.BigDecimal;

public class LeOperand extends MathOperand
{
    public LeOperand(String operator, Operand left, Operand right, String fragment)
    {
        super(operator, left, right, fragment);
    }

    @Override
    protected Object mathBigDecimal(Object leftValue, Object rightValue)
    {
        return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) <= 0;
    }

    @Override
    protected Boolean math(int a, int b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(int a, long b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(int a, float b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(int a, double b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(long a, int b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(long a, long b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(long a, float b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(long a, double b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(float a, int b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(float a, long b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(float a, float b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(float a, double b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(double a, int b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(double a, long b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(double a, float b)
    {
        return a <= b;
    }

    @Override
    protected Boolean math(double a, double b)
    {
        return a <= b;
    }

    @Override
    protected Object processOther(Object leftValue, Object rightValue)
    {
        throw new IllegalStateException("操作数解析出现异常，<= 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
    }
}
