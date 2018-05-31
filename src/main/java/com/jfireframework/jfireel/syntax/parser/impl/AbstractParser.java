package com.jfireframework.jfireel.syntax.parser.impl;

import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.lexer.util.CharType;
import com.jfireframework.jfireel.syntax.Syntax;
import com.jfireframework.jfireel.syntax.parser.Execution;
import com.jfireframework.jfireel.syntax.parser.Parser;

public abstract class AbstractParser implements Parser
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
