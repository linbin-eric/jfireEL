package com.jfireframework.jfireel.parse;

import java.util.Deque;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.parse.impl.CommaParser;
import com.jfireframework.jfireel.parse.impl.ConstantStringParser;
import com.jfireframework.jfireel.parse.impl.IdentifierParser;
import com.jfireframework.jfireel.parse.impl.LeftBracketParser;
import com.jfireframework.jfireel.parse.impl.LeftParenParser;
import com.jfireframework.jfireel.parse.impl.MethodParser;
import com.jfireframework.jfireel.parse.impl.NumberParser;
import com.jfireframework.jfireel.parse.impl.OperatorParser;
import com.jfireframework.jfireel.parse.impl.PropertyParser;
import com.jfireframework.jfireel.parse.impl.RightBracketParser;
import com.jfireframework.jfireel.parse.impl.RightParenParser;
import com.jfireframework.jfireel.parse.impl.SkipIgnoredToken;
import com.jfireframework.jfireel.parse.impl.TypeParser;

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
		        new LeftParenParser(), //
		        new RightParenParser(), //
		        new LeftBracketParser(), //
		        new TypeParser(), //
		        new RightBracketParser(), //
		        new PropertyParser(), //
		        new MethodParser(), //
		        new CommaParser(), //
		        new ConstantStringParser(), //
		        new NumberParser(), //
		        new IdentifierParser(), //
		        new OperatorParser()//
		});
	}
}
