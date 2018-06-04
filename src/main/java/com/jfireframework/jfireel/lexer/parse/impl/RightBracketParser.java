package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.impl.BracketNode;
import com.jfireframework.jfireel.lexer.parse.Invoker;
import com.jfireframework.jfireel.lexer.token.Symbol;
import com.jfireframework.jfireel.lexer.util.OperatorResultUtil;

public class RightBracketParser extends NodeParser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
	{
		if (']' != getChar(offset, el))
		{
			return next.parse(el, offset, nodes, function);
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
