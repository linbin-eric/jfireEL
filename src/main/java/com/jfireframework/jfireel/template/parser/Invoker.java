package com.jfireframework.jfireel.template.parser;

import java.util.Deque;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.template.Template;
import com.jfireframework.jfireel.template.execution.Execution;

public interface Invoker
{
	int scan(String sentence, int offset, Deque<Execution> executions, Template template, StringCache cache);
}
