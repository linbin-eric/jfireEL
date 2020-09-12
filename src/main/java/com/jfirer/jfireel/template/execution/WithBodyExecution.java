package com.jfirer.jfireel.template.execution;

public interface WithBodyExecution extends Execution
{
    void setBody(Execution... executions);

    boolean isBodyNotSet();
}
