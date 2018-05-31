package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.impl.OperatorNode;
import com.jfireframework.jfireel.lexer.parse.Parser;
import com.jfireframework.jfireel.lexer.token.Operator;
import com.jfireframework.jfireel.lexer.util.CharType;

public class OperatorParser implements Parser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		if (CharType.isOperator(CharType.getCurrentChar(offset, el)) == false)
		{
			return offset;
		}
		String literals = new String(new char[] { CharType.getCurrentChar(offset, el), CharType.getCurrentChar(offset + 1, el) });
		if (Operator.literalsOf(literals) != null)
		{
			nodes.push(new OperatorNode(Operator.literalsOf(literals)));
			offset += 2;
			return offset;
		}
		literals = String.valueOf(CharType.getCurrentChar(offset, el));
		if (Operator.literalsOf(literals) != null)
		{
			nodes.push(new OperatorNode(Operator.literalsOf(literals)));
			offset += 1;
			return offset;
		}
		throw new IllegalArgumentException("无法识别:" + literals + "检查:" + el.substring(0, offset));
	}
	
}
