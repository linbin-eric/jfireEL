package com.jfirer.jfireel.template.execution.impl;

import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.template.execution.Execution;

import java.util.Map;

public class ExpressionExecution implements Execution
{
    private Expression expression;
    
    public ExpressionExecution(Expression expression)
    {
        this.expression = expression;
    }
    
    @Override
    public boolean execute(Map<String, Object> variables, StringBuilder cache)
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
