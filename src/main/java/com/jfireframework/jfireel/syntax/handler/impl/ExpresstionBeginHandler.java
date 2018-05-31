package com.jfireframework.jfireel.syntax.handler.impl;

import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.syntax.Syntax;
import com.jfireframework.jfireel.syntax.handler.ScanMode;

public class ExpresstionBeginHandler extends AbstractHandler
{
	
	@Override
	public int scan(String sentence, int offset, Syntax syntax, StringCache cache)
	{
		if (ScanMode.LITERALS != syntax.getMode())
		{
			return offset;
		}
		char c = getCurrent(offset, sentence);
		if (c != '$')
		{
			return offset;
		}
		c = getCurrent(offset + 1, sentence);
		if (c != '{')
		{
			return offset;
		}
		syntax.setMode(ScanMode.EXPRESSION);
		offset += 2;
		extractLiterals(cache, syntax);
		return offset;
	}
	
}
