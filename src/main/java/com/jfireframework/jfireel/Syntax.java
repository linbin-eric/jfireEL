package com.jfireframework.jfireel;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.jfireframework.baseutil.StringUtil;

public class Syntax
{
	private static final int	LITERALS	= 1;
	private static final int	LEXER		= 2;
	
	class Node
	{
		int		type;
		String	literals;
		Lexer	lexer;
		
		public Node(Lexer lexer)
		{
			this.lexer = lexer;
			type = LEXER;
		}
		
		public Node(String literals)
		{
			this.literals = literals;
			type = LITERALS;
		}
		
		public Object calculate(Map<String, Object> params)
		{
			if (type == LITERALS)
			{
				return literals;
			}
			else
			{
				return lexer.calculate(params);
			}
		}
	}
	
	private Node[] nodes;
	
	private Syntax(String sentence)
	{
		if (sentence.contains("${") == false)
		{
			nodes = new Node[] { new Node(Lexer.parse(sentence)) };
		}
		else
		{
			List<Node> list = new LinkedList<Syntax.Node>();
			int pred = 0;
			do
			{
				int start = sentence.indexOf("${", pred);
				if (start == -1)
				{
					String literals = sentence.substring(pred);
					list.add(new Node(literals));
					break;
				}
				else
				{
					int end = sentence.indexOf("}", start);
					if (end == -1)
					{
						throw new IllegalArgumentException(StringUtil.format("{}不符合语法，${没有使用}闭合", sentence));
					}
					String literals = sentence.substring(pred, start);
					list.add(new Node(literals));
					String el = sentence.substring(start + 2, end);
					list.add(new Node(Lexer.parse(el)));
					pred = end + 1;
				}
			} while (pred < sentence.length());
			nodes = list.toArray(new Node[list.size()]);
		}
	}
	
	public String calculate(Map<String, Object> variables)
	{
		StringBuilder builder = new StringBuilder();
		for (Node node : nodes)
		{
			builder.append(node.calculate(variables));
		}
		return builder.toString();
	}
	
	public static Syntax parse(String sentence)
	{
		return new Syntax(sentence);
	}
}
