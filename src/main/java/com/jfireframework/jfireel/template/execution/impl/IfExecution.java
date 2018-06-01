package com.jfireframework.jfireel.template.execution.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.lexer.Expression;
import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.execution.WithBodyExecution;

public class IfExecution implements WithBodyExecution
{
	
	private Expression		conditionLexer;
	private Execution[]		body;
	private Execution		elseExecution;
	private List<Execution>	elseIfExecutions	= new LinkedList<Execution>();
	
	public IfExecution(Expression conditionLexer)
	{
		this.conditionLexer = conditionLexer;
	}
	
	@Override
	public boolean execute(Map<String, Object> variables, StringCache cache)
	{
		Object result = conditionLexer.calculate(variables);
		if (result == null)
		{
			throw new IllegalArgumentException("参数不存在，导致无法计算条件表达式");
		}
		if ((Boolean) result)
		{
			for (Execution each : body)
			{
				each.execute(variables, cache);
			}
		}
		else
		{
			for (Execution execution : elseIfExecutions)
			{
				if (execution.execute(variables, cache))
				{
					return true;
				}
			}
			if (elseExecution != null)
			{
				elseExecution.execute(variables, cache);
			}
		}
		return true;
	}
	
	public void addElseIf(ElseIfExecution execution)
	{
		elseIfExecutions.add(execution);
	}
	
	public void setElse(ElseExecution execution)
	{
		elseExecution = execution;
	}
	
	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public ExecutionType type()
	{
		return ExecutionType.WITH_BODY;
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
