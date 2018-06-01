package com.jfireframework.jfireel.template.execution;

public interface WithBodyExecution extends Execution
{
	void setBody(Execution... executions);
	
	boolean isBodyNotSet();
}
