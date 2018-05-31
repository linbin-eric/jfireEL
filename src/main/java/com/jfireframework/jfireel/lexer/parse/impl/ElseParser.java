package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.impl.ElseNodeImpl;
import com.jfireframework.jfireel.lexer.parse.Parser;
import com.jfireframework.jfireel.lexer.util.CharType;

public class ElseParser implements Parser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		int origin = offset;
		while (CharType.isWhitespace(CharType.getCurrentChar(offset, el)))
		{
			offset++;
		}
		char c1 = CharType.getCurrentChar(offset, el);
		char c2 = CharType.getCurrentChar(offset + 1, el);
		char c3 = CharType.getCurrentChar(offset + 2, el);
		char c4 = CharType.getCurrentChar(offset + 3, el);
		if (c1 != 'e' //
		        || c2 != 'l'//
		        || c3 != 's'//
		        || c4 != 'e')
		{
			return origin;
		}
		offset += 4;
		while (CharType.isWhitespace(CharType.getCurrentChar(offset, el)))
		{
			offset++;
		}
		int end = el.indexOf('{', offset);
		if (end == -1)
		{
			throw new IllegalArgumentException("else判断没有以{结尾" + el.substring(0, offset));
		}
		offset = end + 1;
		nodes.push(new ElseNodeImpl());
		return offset;
	}
	
}
