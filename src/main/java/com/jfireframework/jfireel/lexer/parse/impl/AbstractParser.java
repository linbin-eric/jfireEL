package com.jfireframework.jfireel.lexer.parse.impl;

import com.jfireframework.jfireel.lexer.parse.Parser;
import com.jfireframework.jfireel.lexer.util.CharType;

public abstract class AbstractParser implements Parser
{
	
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
