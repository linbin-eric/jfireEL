package com.jfireframework.jfireel.template.execution.impl;

import com.jfireframework.jfireel.template.execution.Execution;

import java.util.Map;

public class StringExecution implements Execution
{
    private String literals;
    
    public StringExecution(String literals)
    {
        this.literals = literals;
    }
    
    @Override
    public boolean execute(Map<String, Object> variables, StringBuilder cache)
    {
        cache.append(literals);
        return true;
    }
    
    @Override
    public void check()
    {
        
    }
    
}
