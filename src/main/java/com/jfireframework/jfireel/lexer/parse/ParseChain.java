package com.jfireframework.jfireel.lexer.parse;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.parse.impl.CommaParser;
import com.jfireframework.jfireel.lexer.parse.impl.ConstantStringParser;
import com.jfireframework.jfireel.lexer.parse.impl.ElseParser;
import com.jfireframework.jfireel.lexer.parse.impl.EnumParser;
import com.jfireframework.jfireel.lexer.parse.impl.ForEachParser;
import com.jfireframework.jfireel.lexer.parse.impl.IdentifierParser;
import com.jfireframework.jfireel.lexer.parse.impl.IfParser;
import com.jfireframework.jfireel.lexer.parse.impl.LeftBracketParser;
import com.jfireframework.jfireel.lexer.parse.impl.LeftParenParser;
import com.jfireframework.jfireel.lexer.parse.impl.MethodParser;
import com.jfireframework.jfireel.lexer.parse.impl.NumberParser;
import com.jfireframework.jfireel.lexer.parse.impl.OperatorParser;
import com.jfireframework.jfireel.lexer.parse.impl.PropertyParser;
import com.jfireframework.jfireel.lexer.parse.impl.RightBracketParser;
import com.jfireframework.jfireel.lexer.parse.impl.RightParenParser;
import com.jfireframework.jfireel.lexer.parse.impl.SkipIgnoredToken;
import com.jfireframework.jfireel.lexer.parse.impl.TypeParser;

public class ParseChain
{
	private final Parser[] parsers;
	
	public ParseChain(Parser[] parsers)
	{
		this.parsers = parsers;
	}
	
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		for (Parser each : parsers)
		{
			offset = each.parse(el, offset, nodes, function);
		}
		return offset;
	}
	
	public static final ParseChain DEFALT_INSTANCE;
	static
	{
		DEFALT_INSTANCE = new ParseChain(new Parser[] { //
		        new SkipIgnoredToken(), //
		        new IfParser(), //
		        new ElseParser(), //
		        new ForEachParser(), //
		        new LeftParenParser(), //
		        new RightParenParser(), //
		        new LeftBracketParser(), //
		        new TypeParser(), //
		        new RightBracketParser(), //
		        new PropertyParser(), //
		        new EnumParser(), //
		        new MethodParser(), //
		        new CommaParser(), //
		        new ConstantStringParser(), //
		        new NumberParser(), //
		        new IdentifierParser(), //
		        new OperatorParser()//
		});
	}
}
