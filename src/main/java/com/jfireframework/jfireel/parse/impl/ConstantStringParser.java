package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.impl.StringNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.util.CharType;

public class ConstantStringParser implements Parser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		if ('\'' != CharType.getCurrentChar(offset, el))
		{
			return offset;
		}
		offset += 1;
		int origin = offset;
		while (CharType.getCurrentChar(offset, el) != '\'')
		{
			offset++;
		}
		String literals = el.substring(origin, offset);
		nodes.push(new StringNode(literals));
		offset += 1;
		return offset;
	}
	
}
