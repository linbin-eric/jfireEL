package com.jfireframework.jfireel.template.parser;

import com.jfireframework.jfireel.template.Template;
import com.jfireframework.jfireel.template.execution.Execution;

import java.util.Deque;

public interface Invoker
{
    int scan(String sentence, int offset, Deque<Execution> executions, Template template, StringBuilder cache);
}
