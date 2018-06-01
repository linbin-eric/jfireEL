package com.jfireframework.jfireel.template.parser.impl;

import java.util.Deque;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.syntax.ScanMode;
import com.jfireframework.jfireel.template.Template;
import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.parser.Parser;

public class LiteralsParser extends Parser
{
	
	@Override
	public int scan(String sentence, int offset, Deque<Execution> executions, Template template, StringCache cache)
	{
		if (template.getMode() != ScanMode.LITERALS)
		{
			offset = skipWhiteSpace(offset, sentence);
			return offset;
		}
		cache.append(getChar(offset, sentence));
		return offset + 1;
	}
	
}
