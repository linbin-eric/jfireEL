package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.token.Operator;
import com.jfireframework.jfireel.lexer.util.number.PlusUtil;

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
            StringBuilder builder = new StringBuilder();
            builder.append(leftValue).append(rightValue);
            return builder.toString();
        }
        return PlusUtil.calculate((Number) leftValue, (Number) rightValue);
    }
    
}
