package com.jfireframework.jfireel.syntax.handler.impl;

import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.lexer.util.CharType;
import com.jfireframework.jfireel.syntax.Syntax;
import com.jfireframework.jfireel.syntax.handler.Handler;

public class LiteralsHandler implements Handler
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
