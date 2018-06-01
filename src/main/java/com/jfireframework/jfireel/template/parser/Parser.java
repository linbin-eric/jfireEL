package com.jfireframework.jfireel.template.parser;

import java.util.Deque;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.lexer.util.CharType;
import com.jfireframework.jfireel.template.Template;
import com.jfireframework.jfireel.template.exception.IllegalFormatException;
import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.execution.impl.StringExecution;

public abstract class Parser
{
	
	protected static final Execution[] emptyBody = new Execution[0];
	
	public abstract int scan(String sentence, int offset, Deque<Execution> executions, Template template, StringCache cache);
	
	/**
	 * 查询{的位置，如果查询不到抛出异常。如果查询到，则返回{位置+1的结果
	 * 
	 * @param sentence
	 * @param offset
	 * @return
	 */
	protected int findMethodBodyBegin(String sentence, int offset)
	{
		offset = skipWhiteSpace(offset, sentence);
		if ('{' != getChar(offset, sentence))
		{
			throw new IllegalFormatException("方法体没有以{开始", sentence.substring(0, offset));
		}
		offset++;
		return offset;
	}
	
	protected void extractLiterals(StringCache cache, Deque<Execution> executions)
	{
		if (cache.count() != 0)
		{
			Execution execution = new StringExecution(cache.toString());
			cache.clear();
			executions.push(execution);
		}
	}
	
	protected char getChar(int offset, String sentence)
	{
		return CharType.getCurrentChar(offset, sentence);
	}
	
	protected int skipWhiteSpace(int offset, String el)
	{
		while (CharType.isWhitespace(CharType.getCurrentChar(offset, el)))
		{
			offset++;
		}
		return offset;
	}
	
	protected boolean isExecutionBegin(int offset, String sentence)
	{
		char c1 = getChar(offset, sentence);
		char c2 = getChar(offset + 1, sentence);
		if (c1 != '<' || c2 != '%')
		{
			return false;
		}
		return true;
	}
	
	/**
	 * offset当前位置为'(',寻找与之配对的)结束符.返回寻找到)位置。如果找不到，则返回-1
	 * 
	 * @param sentence
	 * @param offset
	 * @param leftBracketIndex
	 * @return
	 */
	protected int findEndRightBracket(String sentence, int offset)
	{
		offset++;
		int length = sentence.length();
		int countForLeftBracket = 0;
		do
		{
			char c = getChar(offset, sentence);
			if (c == '(')
			{
				countForLeftBracket++;
			}
			else if (c == ')')
			{
				if (countForLeftBracket > 0)
				{
					countForLeftBracket--;
				}
				else
				{
					// 此时找到if的括号的封闭括号
					break;
				}
			}
			offset++;
		} while (offset < length);
		if (offset >= length)
		{
			return -1;
		}
		return offset;
	}
	
	/**
	 * 搜索执行语句的结尾，也就是%>所在位置。返回>的坐标。如果没有找到，返回-1
	 * 
	 * @param startIndex
	 * @param sentence
	 * @return
	 */
	protected int findExectionEnd(int startIndex, String sentence)
	{
		int offset = startIndex;
		int length = sentence.length();
		while (offset < length)
		{
			char c = getChar(offset, sentence);
			if (c == '%')
			{
				offset = skipWhiteSpace(offset + 1, sentence);
				c = getChar(offset, sentence);
				if (c == '>')
				{
					return offset;
				}
			}
			offset++;
		}
		return -1;
	}
}
