package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.impl.SymBolNode;
import com.jfireframework.jfireel.lexer.parse.Invoker;
import com.jfireframework.jfireel.lexer.token.Symbol;

public class LeftParenParser extends NodeParser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
	{
		if ('(' != getChar(offset, el))
		{
			return next.parse(el, offset, nodes, function);
		}
		offset += 1;
		nodes.push(new SymBolNode(Symbol.LEFT_PAREN));
		return offset;
	}
	
}
