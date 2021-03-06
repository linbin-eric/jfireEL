package com.jfireframework.jfireel.template.execution.impl;

import com.jfireframework.jfireel.expression.Expression;
import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.execution.WithBodyExecution;

import java.util.Map;

public class ElseIfExecution implements WithBodyExecution
{
    private Expression  expression;
    private Execution[] body;
    
    public ElseIfExecution(Expression expression)
    {
        this.expression = expression;
    }
    
    @Override
    public boolean execute(Map<String, Object> variables, StringBuilder cache)
    {
        if (expression.calculate(variables))
        {
            for (Execution each : body)
            {
                each.execute(variables, cache);
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public void check()
    {
    }
    
    @Override
    public void setBody(Execution... executions)
    {
        body = executions;
    }
    
    @Override
    public boolean isBodyNotSet()
    {
        return body == null;
    }
}
