package com.jfireframework.jfireel.expression.node.impl;

import java.util.Map;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.expression.token.Operator;
import com.jfireframework.jfireel.expression.util.number.PlusUtil;

public class PlusNode extends OperatorResultNode
{
    private static final ThreadLocal<StringCache> LOCAL = new ThreadLocal<StringCache>() {
        protected StringCache initialValue()
        {
            return new StringCache();
            
        };
    };
    
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
            StringCache cache = LOCAL.get();
            cache.append(leftValue).append(rightValue);
            String result = cache.toString();
            cache.clear();
            return result;
        }
        return PlusUtil.calculate((Number) leftValue, (Number) rightValue);
    }
    
    @Override
    public void check()
    {
        // TODO Auto-generated method stub
        
    }
    
}
