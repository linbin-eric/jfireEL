package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.impl.DynamicReflectPropertyNode;
import com.jfireframework.jfireel.lexer.node.impl.DynamicUnsafePropertyNode;
import com.jfireframework.jfireel.lexer.node.impl.StaticPropertyNode;
import com.jfireframework.jfireel.lexer.parse.Parser;
import com.jfireframework.jfireel.lexer.token.Expression;
import com.jfireframework.jfireel.lexer.util.CharType;
import com.jfireframework.jfireel.lexer.util.Functions;

public class PropertyParser implements Parser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		// 如果是后一种情况，意味着此时应该是一个枚举值而不是属性
		if ('.' != CharType.getCurrentChar(offset, el) || (nodes.peek() != null && nodes.peek().type() == Expression.TYPE_ENUM))
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
		// 该情况意味着是方法
		if (c == '(')
		{
			return origin;
		}
		String literals = el.substring(origin + 1, offset);
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
