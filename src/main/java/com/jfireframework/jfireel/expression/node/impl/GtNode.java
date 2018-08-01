package com.jfireframework.jfireel.expression.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.expression.token.Operator;
import com.jfireframework.jfireel.expression.util.number.GtUtil;

public class GtNode extends OperatorResultNode
{
    public GtNode()
    {
        super(Operator.GT);
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
        return GtUtil.calculate((Number) leftValue, (Number) rightValue);
    }
    
    @Override
    public void check()
    {
        // TODO Auto-generated method stub
        
    }
    
}
