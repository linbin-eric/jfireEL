package com.jfirer.jfireel.expression.impl.operand.basic.math;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.basic.BasicOperandImpl;

import java.math.BigDecimal;
import java.util.Map;

abstract class MathOperand extends BasicOperandImpl
{
    public MathOperand(Operand left, Operand right, String fragment)
    {
        super(left, right, fragment);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Object leftValue  = left.calculate(contextParam);
        Object rightValue = right.calculate(contextParam);
        if (leftValue instanceof Number && rightValue instanceof Number)
        {
            if (leftValue instanceof BigDecimal || rightValue instanceof BigDecimal)
            {
                return mathBigDecimal(leftValue, rightValue);
            }
            if (leftValue instanceof Integer || leftValue instanceof Short || leftValue instanceof Byte)
            {
                int a = ((Number) leftValue).intValue();
                if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
                {
                    return math(a, ((Number) rightValue).intValue());
                }
                else if (rightValue instanceof Long b)
                {
                    return math(a, b.longValue());
                }
                else if (rightValue instanceof Float b)
                {
                    return math(a, b.floatValue());
                }
                else if (rightValue instanceof Double b)
                {
                    return math(a, b.doubleValue());
                }
                else
                {
                    throw new IllegalArgumentException();
                }
            }
            else if (leftValue instanceof Long)
            {
                long a = ((Long) leftValue).longValue();
                if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
                {
                    return math(a, ((Number) rightValue).intValue());
                }
                else if (rightValue instanceof Long b)
                {
                    return math(a, b.longValue());
                }
                else if (rightValue instanceof Float b)
                {
                    return math(a, b.floatValue());
                }
                else if (rightValue instanceof Double b)
                {
                    return math(a, b.doubleValue());
                }
                else
                {
                    throw new IllegalArgumentException();
                }
            }
            else if (leftValue instanceof Float number)
            {
                float a = number.floatValue();
                if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
                {
                    return math(a, ((Number) rightValue).intValue());
                }
                else if (rightValue instanceof Long b)
                {
                    return math(a, b.longValue());
                }
                else if (rightValue instanceof Float b)
                {
                    return math(a, b.floatValue());
                }
                else if (rightValue instanceof Double b)
                {
                    return math(a, b.doubleValue());
                }
                else
                {
                    throw new IllegalArgumentException();
                }
            }
            else if (leftValue instanceof Double number)
            {
                double a = number.doubleValue();
                if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
                {
                    return math(a, ((Number) rightValue).intValue());
                }
                else if (rightValue instanceof Long b)
                {
                    return math(a, b.longValue());
                }
                else if (rightValue instanceof Float b)
                {
                    return math(a, b.floatValue());
                }
                else if (rightValue instanceof Double b)
                {
                    return math(a, b.doubleValue());
                }
                else
                {
                    throw new IllegalArgumentException();
                }
            }
            else
            {
                throw new IllegalArgumentException();
            }
        }
        else
        {
            return processOther(leftValue, rightValue);
        }
    }

    protected abstract Object mathBigDecimal(Object leftValue, Object rightValue);

    protected abstract Object math(int a, int b);

    protected abstract Object math(int a, long b);

    protected abstract Object math(int a, float b);

    protected abstract Object math(int a, double b);

    protected abstract Object math(long a, int b);

    protected abstract Object math(long a, long b);

    protected abstract Object math(long a, float b);

    protected abstract Object math(long a, double b);

    protected abstract Object math(float a, int b);

    protected abstract Object math(float a, long b);

    protected abstract Object math(float a, float b);

    protected abstract Object math(float a, double b);

    protected abstract Object math(double a, int b);

    protected abstract Object math(double a, long b);

    protected abstract Object math(double a, float b);

    protected abstract Object math(double a, double b);

    protected abstract Object processOther(Object leftValue, Object rightValue);
}
