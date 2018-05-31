package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.impl.SymBolNode;
import com.jfireframework.jfireel.lexer.parse.Parser;
import com.jfireframework.jfireel.lexer.token.Symbol;
import com.jfireframework.jfireel.lexer.util.CharType;

public class CommaParser implements Parser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		if (',' != CharType.getCurrentChar(offset, el))
		{
			return offset;
		}
		nodes.push(new SymBolNode(Symbol.COMMA));
		offset += 1;
		return offset;
	}
	
}
