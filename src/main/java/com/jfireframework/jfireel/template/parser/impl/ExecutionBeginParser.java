package com.jfireframework.jfireel.template.parser.impl;

import java.util.Deque;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.template.ScanMode;
import com.jfireframework.jfireel.template.Template;
import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.parser.Parser;

public class ExecutionBeginParser extends Parser
{
	
	@Override
	public int scan(String sentence, int offset, Deque<Execution> executions, Template template, StringCache cache)
	{
		if (isExecutionBegin(offset, sentence) == false)
		{
			return offset;
		}
		offset += 2;
		template.setMode(ScanMode.EXECUTION);
		extractLiterals(cache, executions);
		offset = skipWhiteSpace(offset, sentence);
		return offset;
	}
	
}
