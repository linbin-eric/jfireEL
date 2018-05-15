package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.token.Operator;

public class PlusNode extends OperatorResultNode
{
    
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
            return String.valueOf(leftValue) + String.valueOf(rightValue);
        }
        if (leftValue instanceof Integer || leftValue instanceof Short || leftValue instanceof Byte)
        {
            int left = ((Number) leftValue).intValue();
            if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
            {
                return left + ((Number) rightValue).intValue();
            }
            else if (rightValue instanceof Long)
            {
                return left + ((Number) rightValue).longValue();
            }
            else if (rightValue instanceof Float)
            {
                return left + ((Number) rightValue).floatValue();
            }
            else if (rightValue instanceof Double)
            {
                return left + ((Number) rightValue).doubleValue();
            }
            else
            {
                throw new UnsupportedOperationException(rightValue.getClass().getName());
            }
        }
        else if (leftValue instanceof Long)
        {
            long left = ((Number) leftValue).longValue();
            if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
            {
                return left + ((Number) rightValue).intValue();
            }
            else if (rightValue instanceof Long)
            {
                return left + ((Number) rightValue).longValue();
            }
            else if (rightValue instanceof Float)
            {
                return left + ((Number) rightValue).floatValue();
            }
            else if (rightValue instanceof Double)
            {
                return left + ((Number) rightValue).doubleValue();
            }
            else
            {
                throw new UnsupportedOperationException(rightValue.getClass().getName());
            }
        }
        else if (leftValue instanceof Float)
        {
            float left = ((Number) leftValue).floatValue();
            if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
            {
                return left + ((Number) rightValue).intValue();
            }
            else if (rightValue instanceof Long)
            {
                return left + ((Number) rightValue).longValue();
            }
            else if (rightValue instanceof Float)
            {
                return left + ((Number) rightValue).floatValue();
            }
            else if (rightValue instanceof Double)
            {
                return left + ((Number) rightValue).doubleValue();
            }
            else
            {
                throw new UnsupportedOperationException(rightValue.getClass().getName());
            }
        }
        else if (leftValue instanceof Double)
        {
            double left = ((Number) leftValue).floatValue();
            if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
            {
                return left + ((Number) rightValue).intValue();
            }
            else if (rightValue instanceof Long)
            {
                return left + ((Number) rightValue).longValue();
            }
            else if (rightValue instanceof Float)
            {
                return left + ((Number) rightValue).floatValue();
            }
            else if (rightValue instanceof Double)
            {
                return left + ((Number) rightValue).doubleValue();
            }
            else
            {
                throw new UnsupportedOperationException(rightValue.getClass().getName());
            }
        }
        else
        {
            throw new UnsupportedOperationException(leftValue.getClass().getName());
        }
    }
    
}
