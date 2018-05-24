package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.impl.TypeNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.token.Expression;
import com.jfireframework.jfireel.util.CharType;

public class TypeParser implements Parser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		if ('T' == CharType.getCurrentChar(offset, el) && '(' == CharType.getCurrentChar(offset + 1, el))
		{
			offset += 2;
			offset = ignoreWihiespace(offset, el);
			int origin = offset;
			char c;
			while (CharType.isAlphabet(c = CharType.getCurrentChar(offset, el)) || '.' == c || '_' == c || '$' == c)
			{
				offset++;
			}
			int end = offset;
			offset = ignoreWihiespace(offset, el);
			if (')' != CharType.getCurrentChar(offset, el))
			{
				throw new IllegalArgumentException("类型操作没有被)包围，检查:" + el.substring(origin, offset));
			}
			String literals = el.substring(origin, end);
			try
			{
				Class<?> type = Class.forName(literals);
				if (Enum.class.isAssignableFrom(type))
				{
					nodes.push(new TypeNode(type, Expression.TYPE_ENUM));
				}
				else
				{
					nodes.push(new TypeNode(type, Expression.TYPE));
				}
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
			offset += 1;
			return offset;
		}
		else
		{
			return offset;
		}
		
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
