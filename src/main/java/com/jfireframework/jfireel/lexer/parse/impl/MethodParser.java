package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.MethodNode;
import com.jfireframework.jfireel.lexer.node.impl.DynamicCompileMethodNode;
import com.jfireframework.jfireel.lexer.node.impl.DynamicDefaultMethodNode;
import com.jfireframework.jfireel.lexer.node.impl.StaticMethodNode;
import com.jfireframework.jfireel.lexer.parse.Parser;
import com.jfireframework.jfireel.lexer.token.Token;
import com.jfireframework.jfireel.lexer.util.CharType;
import com.jfireframework.jfireel.lexer.util.Functions;

public class MethodParser implements Parser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		if ('.' != CharType.getCurrentChar(offset, el))
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
		// 该情况意味着是属性
		if (c != '(')
		{
			return origin;
		}
		String literals = el.substring(origin + 1, offset);
		CalculateNode beanNode = nodes.pop();
		MethodNode methodNode;
		if (beanNode.type() == Token.TYPE)
		{
			methodNode = new StaticMethodNode(literals, beanNode);
		}
		else
		{
			if (Functions.isMethodInvokeByCompile(function))
			{
				methodNode = new DynamicCompileMethodNode(literals, beanNode);
			}
			else
			{
				methodNode = new DynamicDefaultMethodNode(literals, beanNode);
			}
		}
		nodes.push(methodNode);
		// 当前位置是(，所以位置+1
		offset += 1;
		return offset;
	}
	
}
