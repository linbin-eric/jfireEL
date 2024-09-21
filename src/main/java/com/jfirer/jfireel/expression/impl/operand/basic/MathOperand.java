package com.jfirer.jfireel.expression.impl.operand.basic;

import com.jfirer.jfireel.expression.Operand;

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
            if (leftValue instanceof Byte || leftValue instanceof Short || leftValue instanceof Integer || leftValue instanceof Long)
            {
                long l = ((Number) leftValue).longValue();
                if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer || rightValue instanceof Long)
                {
                    return math(l, ((Number) rightValue).longValue());
                }
                else if (rightValue instanceof Float || rightValue instanceof Double)
                {
                    return math(l, ((Number) rightValue).doubleValue());
                }
                else
                {
                    throw new IllegalArgumentException();
                }
            }
            else if (leftValue instanceof Float || leftValue instanceof Double)
            {
                double v = ((Number) leftValue).doubleValue();
                if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer || rightValue instanceof Long)
                {
                    return math(v, ((Number) rightValue).longValue());
                }
                else if (rightValue instanceof Float || rightValue instanceof Double)
                {
                    return math(v, ((Number) rightValue).doubleValue());
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

    protected abstract Object math(double v, double v1);

    protected abstract Object math(double v, long l);

    protected abstract Object math(long l, double v);

    protected abstract Object math(long l, long l1);

    protected abstract Object processOther(Object leftValue, Object rightValue);
}
