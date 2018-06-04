package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.parse.Invoker;

public class SkipIgnoredToken extends NodeParser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
	{
		offset = skipWhiteSpace(offset, el);
		return next.parse(el, offset, nodes, function);
	}
	
}
