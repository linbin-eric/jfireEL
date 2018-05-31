package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.Lexer;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.impl.IfNodeImpl;
import com.jfireframework.jfireel.lexer.parse.Parser;
import com.jfireframework.jfireel.lexer.util.CharType;

public class IfParser implements Parser
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
		if (c1 != 'i' || c2 != 'f')
		{
			return origin;
		}
		offset += 2;
		while (CharType.isWhitespace(CharType.getCurrentChar(offset, el)))
		{
			offset++;
		}
		int end = el.indexOf('{', offset);
		if (end == -1)
		{
			throw new IllegalArgumentException("if条件判断没有以{结尾" + el.substring(0, offset));
		}
		Lexer lexer = Lexer.parse(el.substring(offset, end));
		IfNodeImpl ifNode = new IfNodeImpl(lexer);
		nodes.push(ifNode);
		offset = end + 1;
		return offset;
	}
	
}
