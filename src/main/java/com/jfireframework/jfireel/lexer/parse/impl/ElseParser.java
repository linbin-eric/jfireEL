package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.Lexer;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.impl.ElseIfNode;
import com.jfireframework.jfireel.lexer.node.impl.ElseNode;
import com.jfireframework.jfireel.lexer.util.CharType;

public class ElseParser extends AbstractParser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		int origin = offset;
		offset = skipWhiteSpace(offset, el);
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
		offset = skipWhiteSpace(offset, el);
		char c = getCurrent(offset, el);
		if (c == 'i' && 'f' == getCurrent(offset + 1, el))
		{
			offset += 2;
			int end = el.indexOf("{", offset);
			if (end != -1)
			{
				ElseIfNode elseIfNode = new ElseIfNode(Lexer.parse(el.substring(offset, end)));
				nodes.push(elseIfNode);
				offset = end + 1;
			}
			else
			{
				throw new IllegalArgumentException("else if判断没有以{结尾" + el.substring(0, offset));
			}
		}
		else if (c == '{')
		{
			nodes.push(new ElseNode());
			offset++;
		}
		else
		{
			throw new IllegalArgumentException("else判断没有以{结尾" + el.substring(0, offset));
		}
		return offset;
	}
	
}
