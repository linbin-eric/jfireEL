package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.impl.SymBolNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.token.Symbol;
import com.jfireframework.jfireel.util.CharType;

public class LeftParenParser implements Parser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		if ('(' != CharType.getCurrentChar(offset, el))
		{
			return offset;
		}
		offset += 1;
		nodes.push(new SymBolNode(Symbol.LEFT_PAREN));
		return offset;
	}
	
}
