package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.Lexer;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.impl.IfNode;

public class IfParser extends AbstractParser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		int origin = offset;
		offset = skipWhiteSpace(offset, el);
		char c1 = getCurrent(offset, el);
		char c2 = getCurrent(offset + 1, el);
		if (c1 != 'i' || c2 != 'f')
		{
			return origin;
		}
		offset += 2;
		offset=skipWhiteSpace(offset, el);
		int end = el.indexOf('{', offset);
		if (end == -1)
		{
			throw new IllegalArgumentException("if条件判断没有以{结尾" + el.substring(0, offset));
		}
		Lexer lexer = Lexer.parse(el.substring(offset, end));
		IfNode ifNode = new IfNode(lexer);
		nodes.push(ifNode);
		offset = end + 1;
		return offset;
	}
	
}
