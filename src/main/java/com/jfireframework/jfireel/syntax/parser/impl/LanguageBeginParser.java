package com.jfireframework.jfireel.syntax.parser.impl;

import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.syntax.Syntax;
import com.jfireframework.jfireel.syntax.parser.ScanMode;

public class LanguageBeginParser extends AbstractParser
{
	
	@Override
	public int scan(String sentence, int offset, Syntax syntax, StringCache cache)
	{
		if (ScanMode.LITERALS != syntax.getMode())
		{
			return offset;
		}
		char c = getCurrent(offset, sentence);
		if (c == '<')
		{
			if ('%' == getCurrent(offset + 1, sentence))
			{
				extractLiterals(cache, syntax);
				syntax.setMode(ScanMode.LANGUAGE);
				offset += 2;
				return offset;
			}
			else
			{
				return offset;
			}
		}
		else
		{
			return offset;
		}
	}
	
}
