package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.parse.Invoker;
import com.jfireframework.jfireel.lexer.util.CharType;

public abstract class NodeParser
{
	
	/**
	 * 在解析节点后返回新的偏移量
	 * 
	 * @param el
	 * @param offset
	 * @param nodes
	 * @return
	 */
	public abstract int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next);
	
	protected char getChar(int offset, String sentence)
	{
		return offset >= sentence.length() ? (char) CharType.EOI : sentence.charAt(offset);
	}
	
	protected int skipWhiteSpace(int offset, String el)
	{
		while (CharType.isWhitespace(getChar(offset, el)))
		{
			offset++;
		}
		return offset;
	}
	
	protected int getIdentifier(int offset, String el)
	{
		int length = 0;
		char c;
		while (CharType.isAlphabet(c = getChar(length + offset, el)) || CharType.isDigital(c))
		{
			length++;
		}
		return length + offset;
	}
}
