package com.jfireframework.jfireel.syntax.parser.impl;

import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.syntax.Syntax;
import com.jfireframework.jfireel.syntax.parser.ScanMode;

public class AfterEndParser extends AbstractParser
{
	
	@Override
	public int scan(String sentence, int offset, Syntax syntax, StringCache cache)
	{
		if (ScanMode.END != syntax.getMode())
		{
			return offset;
		}
		int origin = offset;
		offset = skipWhiteSpace(offset, sentence);
		char c1 = getCurrent(offset, sentence);
		char c2 = getCurrent(offset + 1, sentence);
		char c3 = getCurrent(offset + 2, sentence);
		char c4 = getCurrent(offset + 3, sentence);
		if (c1 != 'e' //
		        || c2 != 'l'//
		        || c3 != 's'//
		        || c4 != 'e')
		{
			return origin;
		}
		cache.append("else");
		syntax.setMode(ScanMode.LANGUAGE);
		return offset + 4;
	}
	
}
