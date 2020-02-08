package com.jfireframework.jfireel.template.execution.impl;

import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.execution.WithBodyExecution;

import java.util.Map;

public class ElseExecution implements WithBodyExecution
{
    private Execution[] body;
    
    @Override
    public boolean execute(Map<String, Object> variables, StringBuilder cache)
    {
        for (Execution each : body)
        {
            each.execute(variables, cache);
        }
        return true;
    }
    
    @Override
    public void check()
    {
        // TODO Auto-generated method stub
        
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
