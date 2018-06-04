package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.impl.SymBolNode;
import com.jfireframework.jfireel.lexer.parse.Invoker;
import com.jfireframework.jfireel.lexer.token.Symbol;

public class LeftBracketParser extends NodeParser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
	{
		if ('[' != getChar(offset, el))
		{
			return next.parse(el, offset, nodes, function);
		}
		nodes.push(new SymBolNode(Symbol.LEFT_BRACKET));
		offset += 1;
		return offset;
	}
	
}
