package com.jfireframework.jfireel;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.node.AssociationNode;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.util.CharType;

public class Syntax
{
	private static final int	LITERALS	= 1;
	private static final int	LEXER		= 2;
	private static final int	LANGUAGE	= 3;
	
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
		
		public Node(Lexer lexer, int type)
		{
			this.lexer = lexer;
			this.type = type;
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
	
	private Node[]		nodes;
	private Deque<Node>	stack	= new LinkedList<Syntax.Node>();
	
	enum ScanMode
	{
	    // 字符串
		LITERALS, //
	    // 语言，主要指if，for
		LANGUAGE, //
	    // 表达式 就是被${}包围起来的内容
		EXPRESSION
	}
	
	private Syntax(String sentence)
	{
		StringCache cache = new StringCache();
		int off = 0;
		int length = sentence.length();
		char c;
		ScanMode mode = ScanMode.LITERALS;
		while (off < length)
		{
			c = getChar(sentence, off);
			if (c == '<')
			{
				if ('%' == getChar(sentence, off + 1))
				{
					if (mode != ScanMode.LITERALS)
					{
						throw new IllegalArgumentException("非法的模板：" + cache.toString());
					}
					Node node = new Node(cache.toString());
					cache.clear();
					stack.push(node);
					mode = ScanMode.LANGUAGE;
					off += 2;
				}
				else
				{
					cache.append(c);
				}
			}
			else if (c == '%')
			{
				if ('>' == getChar(sentence, off + 1))
				{
					if (mode != ScanMode.LANGUAGE)
					{
						throw new IllegalArgumentException("非法的模板：" + cache.toString());
					}
					Node node = new Node(Lexer.parse(cache.toString()), LANGUAGE);
					stack.push(node);
					cache.clear();
					off += 2;
					mode = ScanMode.LITERALS;
				}
				else
				{
					cache.append(c);
					off++;
				}
			}
			else if (c == '}')
			{
				if (mode == ScanMode.LANGUAGE)
				{
					Deque<Node> deque = new LinkedList<Syntax.Node>();
					boolean finish = false;
					Node pop;
					while ((pop = stack.pollFirst()) != null)
					{
						if (pop.type != LANGUAGE)
						{
							deque.push(pop);
						}
						Node[] array = deque.toArray(new Node[0]);
						Syntax syntax = Syntax.parse(array);
						CalculateNode parseResult = pop.lexer.parseResult();
						if (parseResult instanceof AssociationNode == false)
						{
							throw new IllegalArgumentException();
						}
						((AssociationNode) parseResult).setAssociation(syntax);
						stack.push(pop);
						finish = true;
						break;
					}
					if (finish == false)
					{
						throw new IllegalArgumentException();
					}
					off++;
				}
				else if (mode == ScanMode.EXPRESSION)
				{
					Node node = new Node(Lexer.parse(cache.toString()));
					stack.push(node);
					cache.clear();
					off++;
					mode = ScanMode.LITERALS;
				}
				else
				{
					cache.append(c);
					off++;
				}
			}
			else if (c == '$')
			{
				if ('{' != getChar(sentence, off + 1))
				{
					cache.append(c);
					off++;
				}
				else if (mode == ScanMode.LITERALS)
				{
					mode = ScanMode.EXPRESSION;
					off += 2;
					Node node = new Node(cache.toString());
					cache.clear();
					stack.push(node);
				}
				else
				{
					throw new IllegalArgumentException(cache.toString());
				}
			}
			else
			{
				cache.append(c);
				off++;
			}
		}
		Deque<Node> array = new LinkedList<Syntax.Node>();
		while (stack.isEmpty() == false)
		{
			array.push(stack.pollFirst());
		}
		nodes = array.toArray(new Node[0]);
		stack.clear();
	}
	
	private char getChar(String sentence, int off)
	{
		return off < sentence.length() ? sentence.charAt(off) : (char) CharType.EOI;
	}
	
	private Syntax(Node[] nodes)
	{
		this.nodes = nodes;
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
	
	public static Syntax parse(Node[] nodes)
	{
		return new Syntax(nodes);
	}
}
