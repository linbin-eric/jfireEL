package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;

import java.util.Map;

public class GtOperand implements Operand
{
    private Operand left;
    private Operand right;

    public GtOperand(Operand left, Operand right)
    {
        this.left  = left;
        this.right = right;
    }

    @Override
    public Object calculate(Map<String, Object> param)
    {
        Object leftValue  = left.calculate(param);
        Object rightValue = right.calculate(param);
        if (leftValue instanceof Long l1)
        {
            if (rightValue instanceof Long l2)
            {
                return l1.longValue() > l2.longValue();
            }
            else if (rightValue instanceof Double d2)
            {
                return l1.longValue() > d2.doubleValue();
            }
            else
            {
                throw new IllegalStateException();
            }
        }
        else if (leftValue instanceof Double d1)
        {
            if (rightValue instanceof Long l2)
            {
                return d1.longValue() > l2.longValue();
            }
            else if (rightValue instanceof Double d2)
            {
                return d1.longValue() > d2.doubleValue();
            }
            else
            {
                throw new IllegalStateException();
            }
        }
        else
        {
            throw new IllegalStateException();
        }
    }
}
