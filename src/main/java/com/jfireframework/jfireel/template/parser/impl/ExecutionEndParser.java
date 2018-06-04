package com.jfireframework.jfireel.template.parser.impl;

import java.util.Deque;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.template.ScanMode;
import com.jfireframework.jfireel.template.Template;
import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.parser.Invoker;
import com.jfireframework.jfireel.template.parser.Parser;

public class ExecutionEndParser extends Parser
{
	
	@Override
	public int parse(String sentence, int offset, Deque<Execution> executions, Template template, StringCache cache, Invoker next)
	{
		if (template.getMode() != ScanMode.EXECUTION //
		        || '%' != getChar(offset, sentence) //
		        || '>' != getChar(offset + 1, sentence))
		{
			return next.scan(sentence, offset, executions, template, cache);
		}
		template.setMode(ScanMode.LITERALS);
		offset += 2;
		return offset;
	}
	
}
