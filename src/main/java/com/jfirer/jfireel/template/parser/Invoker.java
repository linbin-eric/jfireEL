package com.jfirer.jfireel.template.parser;

import com.jfirer.jfireel.template.Template;
import com.jfirer.jfireel.template.execution.Execution;

import java.util.Deque;

public interface Invoker
{
    int scan(String sentence, int offset, Deque<Execution> executions, Template template, StringBuilder cache);
}
