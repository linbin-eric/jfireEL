package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import com.jfireframework.baseutil.StringUtil;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.impl.TypeNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.util.CharType;

public class TypeParser implements Parser
{
	
	@Override
	public boolean match(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		if ('T' == CharType.getCurrentChar(offset, el) && '(' == CharType.getCurrentChar(offset + 1, el))
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
		offset += 2;
		offset = ignoreWihiespace(offset, el);
		int origin = offset;
		char c;
		while (CharType.isAlphabet(c = CharType.getCurrentChar(offset, el)) || '.' == c)
		{
			offset++;
		}
		int end = offset;
		offset = ignoreWihiespace(offset, el);
		if (')' != CharType.getCurrentChar(offset, el))
		{
			throw new IllegalArgumentException(StringUtil.format("类型操作没有被)包围，检查:{}", el.substring(origin, offset)));
		}
		String literals = el.substring(origin, end);
		try
		{
			Class<?> type = Class.forName(literals);
			nodes.push(new TypeNode(type));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		offset += 1;
		return offset;
	}
	
	private int ignoreWihiespace(int offset, String el)
	{
		while (CharType.isWhitespace(CharType.getCurrentChar(offset, el)))
		{
			offset++;
		}
		return offset;
	}
	
}
