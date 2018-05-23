package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import com.jfireframework.baseutil.StringUtil;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.impl.OperatorNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.token.Operator;
import com.jfireframework.jfireel.util.CharType;

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
		throw new IllegalArgumentException(StringUtil.format("无法识别:{},{}", literals, el.substring(0, offset)));
	}
	
}
