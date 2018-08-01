package com.jfireframework.jfireel.expression.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.expression.token.Operator;
import com.jfireframework.jfireel.expression.util.number.LtUtil;

public class GtEqNode extends OperatorResultNode
{
    public GtEqNode()
    {
        super(Operator.GT_EQ);
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
        return (Boolean) LtUtil.calculate((Number) leftValue, (Number) rightValue) == false;
    }
    
    @Override
    public void check()
    {
        // TODO Auto-generated method stub
        
    }
    
}
