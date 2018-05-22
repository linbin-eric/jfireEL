package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.impl.DynamicReflectPropertyNode;
import com.jfireframework.jfireel.node.impl.DynamicUnsafePropertyNode;
import com.jfireframework.jfireel.node.impl.StaticPropertyNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.token.Expression;
import com.jfireframework.jfireel.util.CharType;
import com.jfireframework.jfireel.util.Functions;

public class PropertyParser implements Parser
{
	
	@Override
	public boolean match(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		if ('.' != CharType.getCurrentChar(offset, el))
		{
			return false;
		}
		offset += 1;
		char c;
		while (CharType.isAlphabet(c = CharType.getCurrentChar(offset, el)) || CharType.isDigital(c))
		{
			offset++;
		}
		// 该情况意味着是方法
		if (c == '(')
		{
			return false;
		}
		// 不是方法，则必然是属性
		else
		{
			return true;
		}
	}
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		offset += 1;
		int origin = offset;
		char c;
		while (CharType.isAlphabet(c = CharType.getCurrentChar(offset, el)) || CharType.isDigital(c))
		{
			offset++;
		}
		String literals = el.substring(origin, offset);
		CalculateNode beanNode = nodes.pop();
		CalculateNode current;
		if (beanNode.type() == Expression.TYPE)
		{
			current = new StaticPropertyNode(literals, beanNode);
		}
		else
		{
			if (Functions.isPropertyFetchByUnsafe(function))
			{
				current = new DynamicUnsafePropertyNode(literals, beanNode, Functions.isRecognizeEveryTime(function));
			}
			else if (Functions.isPropertyFetchByReflect(function))
			{
				current = new DynamicReflectPropertyNode(literals, beanNode, Functions.isRecognizeEveryTime(function));
			}
			else
			{
				current = new DynamicReflectPropertyNode(literals, beanNode, Functions.isRecognizeEveryTime(function));
				
			}
		}
		nodes.push(current);
		return offset;
	}
	
}
