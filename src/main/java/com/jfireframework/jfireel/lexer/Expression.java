package com.jfireframework.jfireel.lexer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.parse.ParseChain;
import com.jfireframework.jfireel.lexer.util.OperatorResultUtil;

public class Expression
{
	private CalculateNode			parseNode;
	private Deque<CalculateNode>	nodes	= new LinkedList<CalculateNode>();
	private int						offset	= 0;
	private String					el;
	private int						function;
	private ParseChain				parseChain;
	
	public static Expression parse(String el)
	{
		return new Expression(el, 0, ParseChain.DEFALT_INSTANCE);
	}
	
	public static Expression parse(String el, int function)
	{
		return new Expression(el, function, ParseChain.DEFALT_INSTANCE);
	}
	
	public static Expression parse(String el, int function, ParseChain parseChain)
	{
		return new Expression(el, function, parseChain);
	}
	
	private Expression(String el, int function, ParseChain parseChain)
	{
		this.parseChain = parseChain;
		this.el = el;
		this.function = function;
		scan();
	}
	
	private void scan()
	{
		while (true)
		{
			if (isEnd())
			{
				break;
			}
			int pred = offset;
			offset = parseChain.parse(el, offset, nodes, function);
			if (pred == offset)
			{
				throw new IllegalArgumentException("无法识别的表达式，解析过程预见无法识别的字符:" + el.substring(0, offset));
			}
		}
		List<CalculateNode> list = new ArrayList<CalculateNode>();
		CalculateNode tmp;
		while ((tmp = nodes.pollFirst()) != null)
		{
			list.add(0, tmp);
		}
		parseNode = OperatorResultUtil.aggregate(list, function, el, offset);
		clear();
	}
	
	private void clear()
	{
		nodes = null;
		parseChain = null;
		el = null;
	}
	
	private boolean isEnd()
	{
		return offset >= el.length();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T calculate(Map<String, Object> variables)
	{
		return (T) parseNode.calculate(variables);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T calculate()
	{
		return (T) parseNode.calculate(null);
	}
	
	public CalculateNode parseResult()
	{
		return parseNode;
	}
}
