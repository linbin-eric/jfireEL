package com.jfireframework.jfireel.syntax.handler.impl;

import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.lexer.util.CharType;
import com.jfireframework.jfireel.syntax.Syntax;
import com.jfireframework.jfireel.syntax.handler.Execution;
import com.jfireframework.jfireel.syntax.handler.Handler;

public abstract class AbstractHandler implements Handler
{
	
	protected void extractLiterals(StringCache cache, Syntax syntax)
	{
		if (cache.count() != 0)
		{
			Execution node = new Execution(cache.toString());
			cache.clear();
			syntax.getStack().push(node);
		}
	}
	
	protected char getCurrent(int offset, String sentence)
	{
		return CharType.getCurrentChar(offset, sentence);
	}
	
	protected int skipWhiteSpace(int offset, String el)
	{
		while (CharType.isWhitespace(CharType.getCurrentChar(offset, el)))
		{
			offset++;
		}
		return offset;
	}
}
