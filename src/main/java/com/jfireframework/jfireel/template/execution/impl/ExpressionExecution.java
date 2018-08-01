package com.jfireframework.jfireel.template.execution.impl;

import java.util.Map;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.expression.Expression;
import com.jfireframework.jfireel.template.execution.Execution;

public class ExpressionExecution implements Execution
{
    private Expression expression;
    
    public ExpressionExecution(Expression expression)
    {
        this.expression = expression;
    }
    
    @Override
    public boolean execute(Map<String, Object> variables, StringCache cache)
    {
        Object result = expression.calculate(variables);
        if (result != null)
        {
            cache.append(result);
        }
        return true;
    }
    
    @Override
    public void check()
    {
        // TODO Auto-generated method stub
        
    }
    
}
