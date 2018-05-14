package com.jfireframework.jfireel;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.EqualNode;
import com.jfireframework.jfireel.node.KeywordNode;
import com.jfireframework.jfireel.node.MethodNode;
import com.jfireframework.jfireel.node.MutliNode;
import com.jfireframework.jfireel.node.NotEqualNode;
import com.jfireframework.jfireel.node.NumberNode;
import com.jfireframework.jfireel.node.OperatorNode;
import com.jfireframework.jfireel.node.OperatorResultNode;
import com.jfireframework.jfireel.node.PlusNode;
import com.jfireframework.jfireel.node.PropertyNode;
import com.jfireframework.jfireel.node.StringNode;
import com.jfireframework.jfireel.node.SymBolNode;
import com.jfireframework.jfireel.node.VariableNode;

public class Lexer
{
	private Deque<CalculateNode>	nodes		= new LinkedList<CalculateNode>();
	private Deque<CalculateNode>	tmpStack	= new LinkedList<CalculateNode>();
	private int						offset		= 0;
	private String					el;
	
	public static Lexer parse(String el)
	{
		return new Lexer(el);
	}
	
	private Lexer(String el)
	{
		this.el = el;
		scan();
	}
	
	private void scan()
	{
		while (true)
		{
			skipIgnoredToken();
			if (isEnd())
			{
				break;
			}
			else if (isIdentifierBegin())
			{
				scanIdentifier();
			}
			else if (isLeftParen())
			{
				scanLeftParen();
			}
			else if (isRightParen())
			{
				scanRightParen();
			}
			else if (isDot())
			{
				scanDot();
			}
			else if (isComma())
			{
				scanComma();
			}
			else if (isCharactersBegin())
			{
				scanCharacters();
			}
			else if (isNumberBegin())
			{
				scanNumber();
			}
			else if (isOperatorBegin())
			{
				scanOperator();
			}
		}
		if (tmpStack.isEmpty() == false)
		{
			throw new IllegalArgumentException(el);
		}
		CalculateNode tmp;
		while ((tmp = nodes.pollFirst()) != null)
		{
			tmpStack.push(tmp);
		}
		List<CalculateNode> list = new ArrayList<CalculateNode>();
		while ((tmp = tmpStack.pollFirst()) != null)
		{
			list.add(tmp);
		}
		nodes.push(processParam(list));
	}
	
	public Object calculate(Map<String, Object> variables)
	{
		return nodes.getFirst().calculate(variables);
	}
	
	private boolean isOperatorBegin()
	{
		return CharType.isOperator(getCurrentChar(0));
	}
	
	private void scanOperator()
	{
		String literals = el.substring(offset, offset + 1);
		if (Operator.literalsOf(literals) != null)
		{
			nodes.push(new OperatorNode(Operator.literalsOf(literals)));
			offset += 1;
			return;
		}
		literals = el.substring(offset, offset + 2);
		if (Operator.literalsOf(literals) != null)
		{
			nodes.push(new OperatorNode(Operator.literalsOf(literals)));
			offset += 2;
			return;
		}
	}
	
	private boolean isCharactersBegin()
	{
		return '\'' == getCurrentChar(0);
	}
	
	private void scanCharacters()
	{
		int length = 1;
		while (getCurrentChar(length) != '\'')
		{
			length += 1;
		}
		String characters = el.substring(offset + 1, offset + length);
		nodes.push(new StringNode(characters));
		offset += length + 1;
	}
	
	private boolean isComma()
	{
		return ',' == getCurrentChar(0);
	}
	
	private void scanComma()
	{
		nodes.push(new SymBolNode(Symbol.COMMA));
		offset += 1;
	}
	
	private void scanIdentifier()
	{
		nodes.push(parseIdentifier());
	}
	
	private boolean isRightParen()
	{
		return ')' == getCurrentChar(0);
	}
	
	private void scanRightParen()
	{
		CalculateNode pred;
		while ((pred = nodes.peek()) != null)
		{
			if (pred.type() != Symbol.LEFT_PAREN && pred.type() != Expression.METHOD)
			{
				tmpStack.push(nodes.pop());
			}
			else
			{
				break;
			}
		}
		if (pred == null)
		{
			throw new IllegalArgumentException(el.substring(0, offset));
		}
		if (pred.type() == Expression.METHOD)
		{
			CalculateNode node;
			List<CalculateNode> list = new LinkedList<CalculateNode>();
			List<CalculateNode> argsNodes = new LinkedList<CalculateNode>();
			while ((node = tmpStack.pollFirst()) != null)
			{
				if (node.type() == Symbol.COMMA)
				{
					argsNodes.add(processParam(list));
					list.clear();
				}
				else
				{
					list.add(node);
				}
			}
			if (list.isEmpty() == false)
			{
				argsNodes.add(processParam(list));
			}
			((MethodNode) pred).setArgsNodes(argsNodes.toArray(new CalculateNode[argsNodes.size()]));
			offset += 1;
		}
		else
		{
			// 如果是括号左右包围，则弹出左括号
			nodes.pop();
			CalculateNode node;
			List<CalculateNode> list = new LinkedList<CalculateNode>();
			while ((node = tmpStack.pollFirst()) != null)
			{
				list.add(node);
			}
			nodes.push(processParam(list));
			offset += 1;
		}
	}
	
	private CalculateNode processParam(List<CalculateNode> list)
	{
		// 只有一个参数的情况
		if (list.size() == 1)
		{
			return list.get(0);
		}
		// 只有一个操作符
		else if (list.size() == 3)
		{
			// 第二个node是操作符
			if (Operator.class.isAssignableFrom(list.get(1).type().getClass()))
			{
				return buildOperatorResultNode(list.get(0), list.get(1), list.get(2));
			}
			// 否则是非法情况
			else
			{
				throw new IllegalArgumentException(el.substring(0, offset));
			}
		}
		// 具备至少2个操作符
		else if (list.size() >= 5)
		{
			// 优先操作乘法，除法，取余
			for (int i = 0; i < list.size();)
			{
				CalculateType type = list.get(i).type();
				if (type == Operator.MULTI || type == Operator.DIVISION || type == Operator.PERCENT)
				{
					if (i > 0 && list.size() > i + 1//
					        && Operator.isOperator(list.get(i - 1).type()) == false//
					        && Operator.isOperator(list.get(i + 1).type()) == false//
					)
					{
						OperatorResultNode resultNode = buildOperatorResultNode(list.get(i - 1), list.get(i), list.get(i + 1));
						list.remove(i - 1);
						list.remove(i - 1);
						list.remove(i - 1);
						// 执行3次消除操作符和2个操作数
						list.add(i - 1, resultNode);
					}
					else
					{
						throw new IllegalArgumentException(el.substring(0, offset));
					}
				}
				else
				{
					i++;
				}
			}
			for (int i = 0; i < list.size();)
			{
				CalculateType type = list.get(i).type();
				if (Operator.isOperator(type))
				{
					if (i > 0 && list.size() > i + 1//
					        && Operator.isOperator(list.get(i - 1).type()) == false//
					        && Operator.isOperator(list.get(i + 1).type()) == false//
					)
					{
						OperatorResultNode resultNode = buildOperatorResultNode(list.get(i - 1), list.get(i), list.get(i + 1));
						list.remove(i - 1);
						list.remove(i - 1);
						list.remove(i - 1);
						// 执行3次消除操作符和2个操作数
						list.add(i - 1, resultNode);
					}
					else
					{
						throw new IllegalArgumentException(el.substring(0, offset));
					}
				}
				else
				{
					i++;
				}
			}
			if (list.size() != 1)
			{
				throw new IllegalArgumentException(el.substring(0, offset));
			}
			return list.get(0);
		}
		else
		{
			throw new IllegalArgumentException(el.substring(0, offset));
		}
	}
	
	private OperatorResultNode buildOperatorResultNode(CalculateNode leftNode, CalculateNode operatorNode, CalculateNode rightNode)
	{
		OperatorResultNode resultNode = null;
		switch ((Operator) operatorNode.type())
		{
			case PLUS:
				resultNode = new PlusNode();
				break;
			case MULTI:
				resultNode = new MutliNode();
				break;
			case EQ:
				resultNode = new EqualNode();
				break;
			case NOT_EQ:
				resultNode = new NotEqualNode();
				break;
			default:
				break;
		}
		resultNode.addLeftOperand(leftNode);
		resultNode.addRightOperand(rightNode);
		return resultNode;
	}
	
	private boolean isLeftParen()
	{
		return '(' == getCurrentChar(0);
	}
	
	private void scanLeftParen()
	{
		offset += 1;
		nodes.push(new SymBolNode(Symbol.LEFT_PAREN));
	}
	
	private boolean isIdentifierBegin()
	{
		return CharType.isAlphabet(getCurrentChar(0));
	}
	
	private boolean isNumberBegin()
	{
		return CharType.isDigital(getCurrentChar(0)) //
		        || ('-' == getCurrentChar(0) && CharType.isDigital(getCurrentChar(1)));
	}
	
	private void scanNumber()
	{
		char c = getCurrentChar(0);
		int length = 0;
		length += 1;
		boolean hasDot = false;
		while (CharType.isDigital(c = getCurrentChar(length)) || (hasDot == false && c == '.'))
		{
			length++;
			if (c == '.')
			{
				hasDot = true;
			}
		}
		if (c == '.')
		{
			throw new IllegalArgumentException(el.substring(offset, offset + length));
		}
		nodes.push(new NumberNode(el.substring(offset, offset + length)));
		offset += length;
	}
	
	private boolean isEnd()
	{
		return offset >= el.length();
	}
	
	protected final char getCurrentChar(final int offset)
	{
		return this.offset + offset >= el.length() ? (char) CharType.EOI : el.charAt(this.offset + offset);
	}
	
	private void skipIgnoredToken()
	{
		int length = 0;
		while (CharType.isWhitespace(getCurrentChar(length)))
		{
			length++;
		}
		offset += length;
	}
	
	private boolean isDot()
	{
		return '.' == getCurrentChar(0);
	}
	
	private void scanDot()
	{
		offset += 1;
		int length = 0;
		char c;
		while (CharType.isAlphabet(c = getCurrentChar(length)) || CharType.isDigital(c))
		{
			length++;
		}
		if (c == '(')
		{
			String literals = el.substring(offset, offset + length);
			CalculateNode pred = nodes.pop();
			MethodNode methodNode = new MethodNode(literals, pred);
			nodes.push(methodNode);
			offset += length + 1;
		}
		// 不是方法，则必然是属性
		else
		{
			String literals = el.substring(offset, offset + length);
			CalculateNode pred = nodes.pop();
			PropertyNode current = new PropertyNode(literals, pred);
			nodes.push(current);
			offset += length;
		}
	}
	
	private CalculateNode parseIdentifier()
	{
		int length = 0;
		char c;
		while (CharType.isAlphabet(c = getCurrentChar(length)) || CharType.isDigital(c))
		{
			length++;
		}
		String literals = el.substring(offset, offset + length);
		offset += length;
		if (DefaultKeyWord.getDefaultKeyWord(literals) != null)
		{
			return new KeywordNode(literals);
		}
		else
		{
			return new VariableNode(literals);
		}
	}
	
}
