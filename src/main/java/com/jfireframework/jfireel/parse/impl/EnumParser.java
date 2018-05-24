package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.token.Expression;
import com.jfireframework.jfireel.util.CharType;

public class EnumParser implements Parser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		// 如果是后一种情况，意味着此时应该是一个枚举值而不是属性
		if ('.' != CharType.getCurrentChar(offset, el) || (nodes.peek() != null && nodes.peek().type() != Expression.TYPE_ENUM))
		{
			return offset;
		}
		int origin = offset;
		offset += 1;
		char c;
		while (CharType.isAlphabet(c = CharType.getCurrentChar(offset, el)) || CharType.isDigital(c))
		{
			offset++;
		}
		if (c == '(')
		{
			throw new IllegalArgumentException("非法的el表达式，检查:" + el.substring(origin, offset));
		}
		String literals = el.substring(origin + 1, offset);
		CalculateNode beanNode = nodes.pop();
		CalculateNode current = new EnumNode(beanNode, literals);
		nodes.push(current);
		return offset;
	}
	
}
