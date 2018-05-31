package com.jfireframework.jfireel.syntax.handler.impl;

import java.util.Deque;
import java.util.LinkedList;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.lexer.Lexer;
import com.jfireframework.jfireel.lexer.node.AssociationNode;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.impl.ElseIfNode;
import com.jfireframework.jfireel.lexer.node.impl.ElseNode;
import com.jfireframework.jfireel.lexer.node.impl.IfNode;
import com.jfireframework.jfireel.lexer.token.Expression;
import com.jfireframework.jfireel.syntax.Syntax;
import com.jfireframework.jfireel.syntax.handler.Execution;
import com.jfireframework.jfireel.syntax.handler.ScanMode;

public class RightBraceHandler extends AbstractHandler
{
	
	@Override
	public int scan(String sentence, int offset, Syntax syntax, StringCache cache)
	{
		if (ScanMode.LANGUAGE != syntax.getMode() && ScanMode.EXPRESSION != syntax.getMode())
		{
			return offset;
		}
		char c = getCurrent(offset, sentence);
		if (c != '}')
		{
			return offset;
		}
		ScanMode mode = syntax.getMode();
		if (mode == ScanMode.LANGUAGE)
		{
			Deque<Execution> deque = new LinkedList<Execution>();
			boolean finish = false;
			Execution pop;
			while ((pop = syntax.getStack().pollFirst()) != null)
			{
				if (pop.type() != Execution.LANGUAGE)
				{
					deque.push(pop);
					continue;
				}
				Execution[] array = deque.toArray(new Execution[0]);
				Syntax subSyntax = Syntax.parse(array);
				CalculateNode parseResult = pop.lexer().parseResult();
				if (parseResult instanceof AssociationNode == false)
				{
					throw new IllegalArgumentException();
				}
				((AssociationNode) parseResult).setAssociation(subSyntax);
				if (parseResult.type() == Expression.ELSE)
				{
					pop = syntax.getStack().pollFirst();
					if (pop == null || pop.lexer() == null || pop.lexer().parseResult().type() != Expression.IF)
					{
						throw new IllegalArgumentException("else 节点的前向节点必须是if节点");
					}
					((IfNode) pop.lexer().parseResult()).setElseNode((ElseNode) parseResult);
				}
				else if (parseResult.type() == Expression.ELSE_IF)
				{
					pop = syntax.getStack().pollFirst();
					if (pop == null || pop.lexer() == null || pop.lexer().parseResult().type() != Expression.IF)
					{
						throw new IllegalArgumentException("else if 节点的前向节点必须是if节点");
					}
					((IfNode) pop.lexer().parseResult()).addElseIfNode((ElseIfNode) parseResult);
				}
				pop.changeTypeToLexer();
				syntax.getStack().push(pop);
				finish = true;
				break;
			}
			if (finish == false)
			{
				throw new IllegalArgumentException();
			}
			offset++;
			syntax.setMode(ScanMode.END);
			return offset;
		}
		else if (mode == ScanMode.EXPRESSION)
		{
			Execution execution = new Execution(Lexer.parse(cache.toString()), Execution.LEXER);
			syntax.getStack().push(execution);
			cache.clear();
			offset++;
			syntax.setMode(ScanMode.LITERALS);
			return offset;
		}
		else
		{
			return offset;
		}
	}
	
}
