package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.impl.KeywordNode;
import com.jfireframework.jfireel.node.impl.VariableNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.token.DefaultKeyWord;
import com.jfireframework.jfireel.util.CharType;

public class IdentifierParser implements Parser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		if (CharType.isAlphabet(CharType.getCurrentChar(offset, el)) == false)
		{
			return offset;
		}
		return parseIdentifier(el, offset, nodes);
	}
	
	private int parseIdentifier(String el, int offset, Deque<CalculateNode> nodes)
	{
		int length = 0;
		char c;
		while (CharType.isAlphabet(c = CharType.getCurrentChar(length + offset, el)) || CharType.isDigital(c))
		{
			length++;
		}
		String literals = el.substring(offset, offset + length);
		offset += length;
		if (DefaultKeyWord.getDefaultKeyWord(literals) != null)
		{
			nodes.push(new KeywordNode(literals));
		}
		else
		{
			nodes.push(new VariableNode(literals));
		}
		return offset;
	}
	
}
