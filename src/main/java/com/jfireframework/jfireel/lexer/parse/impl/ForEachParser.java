package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.impl.ForEachNode;
import com.jfireframework.jfireel.lexer.node.impl.VariableNode;

public class ForEachParser extends AbstractParser
{
	
	@Override
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		int origin = offset;
		offset = skipWhiteSpace(offset, el);
		char c1 = getCurrent(offset, el);
		char c2 = getCurrent(offset + 1, el);
		char c3 = getCurrent(offset + 2, el);
		if (c1 != 'f' || c2 != 'o' || c3 != 'r')
		{
			return origin;
		}
		offset = skipWhiteSpace(offset+3, el);
		if (getCurrent(offset, el) != '(')
		{
			throw new IllegalArgumentException("for循环语句语法错误，请检查:" + el.substring(0, offset));
		}
		int end = getIdentifier(offset+=1, el);
		String itemName = el.substring(offset, end);
		offset = skipWhiteSpace(end + 1, el);
		c1 = getCurrent(offset, el);
		c2 = getCurrent(offset + 1, el);
		if (c1 != 'i' || c2 != 'n')
		{
			throw new IllegalArgumentException("for循环语句语法错误，请检查:" + el.substring(0, offset));
		}
		offset = skipWhiteSpace(offset + 2, el);
		end = getIdentifier(offset, el);
		String collectionName = el.substring(offset, end);
		CalculateNode collectionNode = new VariableNode(collectionName);
		ForEachNode forEachNode = new ForEachNode(itemName, collectionNode);
		nodes.push(forEachNode);
		offset = skipWhiteSpace(end, el);
		if (')' != getCurrent(offset, el))
		{
			throw new IllegalArgumentException("for循环语句语法错误，请检查:" + el.substring(0, offset));
		}
		offset = skipWhiteSpace(offset + 1, el);
		if ('{' != getCurrent(offset, el))
		{
			throw new IllegalArgumentException("for循环语句语法错误，请检查:" + el.substring(0, offset));
		}
		return offset + 1;
		
	}
	
}
