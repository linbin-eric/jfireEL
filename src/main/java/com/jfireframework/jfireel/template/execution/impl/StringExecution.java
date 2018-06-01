package com.jfireframework.jfireel.template.execution.impl;

import java.util.Map;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.template.execution.Execution;

public class StringExecution implements Execution
{
	private String literals;
	
	public StringExecution(String literals)
	{
		this.literals = literals;
	}
	
	@Override
	public boolean execute(Map<String, Object> variables, StringCache cache)
	{
		cache.append(literals);
		return true;
	}
	
	@Override
	public void check()
	{
		
	}
	
	@Override
	public ExecutionType type()
	{
		return ExecutionType.LITERALS;
	}
	
}
