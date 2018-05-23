package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import com.jfireframework.baseutil.StringUtil;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.impl.NumberNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.token.Operator;
import com.jfireframework.jfireel.util.CharType;

public class NumberParser implements Parser
{
	
	private boolean match(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		if ('-' == CharType.getCurrentChar(offset, el))
		{
			// 这种情况下，-是一个操作符
			if (nodes.peek() != null && nodes.peek().type() instanceof Operator == false)
			{
				return false;
			}
			// 这种情况下，-代表是一个负数
			if (CharType.isDigital(CharType.getCurrentChar(offset + 1, el)))
			{
				return true;
			}
			else
			{
				throw new IllegalArgumentException(StringUtil.format("无法识别的-符号，不是负数也不是操作符,问题区间:{}", el.substring(0, offset)));
			}
		}
		else if (CharType.isDigital(CharType.getCurrentChar(offset, el)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		if (match(el, offset, nodes, function) == false)
		{
			return offset;
		}
		int origin = offset;
		char c = CharType.getCurrentChar(offset, el);
		if (c == '-')
		{
			offset += 1;
		}
		boolean hasDot = false;
		while (CharType.isDigital(c = CharType.getCurrentChar(offset, el)) || (hasDot == false && c == '.'))
		{
			offset++;
			if (c == '.')
			{
				hasDot = true;
			}
		}
		if (c == '.')
		{
			throw new IllegalArgumentException(StringUtil.format("非法的负数格式,问题区间:{}", el.substring(origin, offset)));
		}
		String literals = el.substring(origin, offset);
		nodes.push(new NumberNode(literals));
		return offset;
	}
	
}
