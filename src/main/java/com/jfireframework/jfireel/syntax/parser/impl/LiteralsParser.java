package com.jfireframework.jfireel.syntax.parser.impl;

import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.lexer.util.CharType;
import com.jfireframework.jfireel.syntax.Syntax;
import com.jfireframework.jfireel.syntax.parser.Parser;

public class LiteralsParser implements Parser
{
	
	@Override
	public int scan(String sentence, int offset, Syntax syntax, StringCache cache)
	{
		if (offset < sentence.length())
		{
			cache.append(CharType.getCurrentChar(offset, sentence));
			offset++;
			return offset;
		}
		else
		{
			return offset;
		}
	}
	
}
