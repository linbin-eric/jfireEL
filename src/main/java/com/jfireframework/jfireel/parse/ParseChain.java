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

public class ParseChain
{
	private Parser[] parsers;
	
	public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
	{
		for (Parser each : parsers)
		{
			if (each.match(el, offset, nodes, function))
			{
				offset = each.parse(el, offset, nodes, function);
			}
		}
		return offset;
	}
	
	public void setParsers(Parser... parsers)
	{
		this.parsers = parsers;
	}
	
	public static final ParseChain DEFALT_INSTANCE;
	static
	{
		DEFALT_INSTANCE = new ParseChain();
		DEFALT_INSTANCE.setParsers(//
		        new SkipIgnoredToken(), //
		        new IdentifierParser(), //
		        new LeftParenParser(), //
		        new RightParenParser(), //
		        new LeftBracketParser(), //
		        new RightBracketParser(), //
		        new PropertyParser(), //
		        new MethodParser(), //
		        new CommaParser(), //
		        new ConstantStringParser(), //
		        new NumberParser(), //
		        new OperatorParser()//
		);
	}
}
