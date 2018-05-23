package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.impl.BracketNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.token.Symbol;
import com.jfireframework.jfireel.util.OperatorResultUtil;
import com.jfireframework.jfireel.util.CharType;

public class RightBracketParser implements Parser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		if (']' != CharType.getCurrentChar(offset, el))
		{
			return offset;
		}
		List<CalculateNode> list = new LinkedList<CalculateNode>();
		CalculateNode pred;
		while ((pred = nodes.pollFirst()) != null)
		{
			if (pred.type() != Symbol.LEFT_BRACKET)
			{
				list.add(0, pred);
			}
			else
			{
				break;
			}
		}
		if (pred == null)
		{
			throw new IllegalArgumentException(el.substring(0, offset));
		}
		CalculateNode valueNode = OperatorResultUtil.aggregate(list, function, el, offset);
		CalculateNode beanNode = nodes.pollFirst();
		if (beanNode == null)
		{
			throw new UnsupportedOperationException();
		}
		nodes.push(new BracketNode(beanNode, valueNode));
		offset += 1;
		return offset;
	}
	
}
