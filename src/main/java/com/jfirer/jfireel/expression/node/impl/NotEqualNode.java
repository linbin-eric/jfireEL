package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.token.Operator;
import com.jfirer.jfireel.expression.util.number.EqUtil;

import java.util.Map;

public class NotEqualNode extends OperatorResultNode
{

    public NotEqualNode()
    {
        super(Operator.NOT_EQ);
    }

    @Override
    public Object calculate(Map<String, Object> variables)
    {
        Object leftValue  = leftOperand.calculate(variables);
        Object rightValue = rightOperand.calculate(variables);
        if (leftValue == null && rightValue == null)
        {
            return false;
        }
        else if (leftValue == null && rightValue != null)
        {
            return true;
        }
        else if (leftValue != null && rightValue == null)
        {
            return true;
        }
        else
        {
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                return EqUtil.calculate((Number) leftValue, (Number) rightValue) == false;
            }
            else
            {
                return leftValue.equals(rightValue) == false;
            }
        }
    }
}
