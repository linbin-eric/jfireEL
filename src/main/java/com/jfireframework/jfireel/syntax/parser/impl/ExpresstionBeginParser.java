package com.jfireframework.jfireel.syntax.parser.impl;

import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.syntax.Syntax;
import com.jfireframework.jfireel.syntax.parser.ScanMode;

public class ExpresstionBeginParser extends AbstractParser
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
