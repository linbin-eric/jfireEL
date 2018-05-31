package com.jfireframework.jfireel.syntax;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.syntax.handler.Execution;
import com.jfireframework.jfireel.syntax.handler.Handler;
import com.jfireframework.jfireel.syntax.handler.ScanMode;
import com.jfireframework.jfireel.syntax.handler.impl.AfterEndHandler;
import com.jfireframework.jfireel.syntax.handler.impl.ExpresstionBeginHandler;
import com.jfireframework.jfireel.syntax.handler.impl.LanguageBeginHandler;
import com.jfireframework.jfireel.syntax.handler.impl.LanguageEndHandler;
import com.jfireframework.jfireel.syntax.handler.impl.LiteralsHandler;
import com.jfireframework.jfireel.syntax.handler.impl.RightBraceHandler;

public class Syntax
{
	private Execution[]			nodes;
	private Deque<Execution>	stack	= new LinkedList<Execution>();
	private ScanMode			mode;
	private Handler[]			handlers	= new Handler[] {				//
	        new LanguageBeginHandler(),									//
	        new LanguageEndHandler(),									//
	        new ExpresstionBeginHandler(),								//
	        new RightBraceHandler(),										//
	        new AfterEndHandler(),										//
	        new LiteralsHandler() };
	
	private Syntax(String sentence)
	{
		StringCache cache = new StringCache();
		int offset = 0;
		int length = sentence.length();
		mode = ScanMode.LITERALS;
		while (offset < length)
		{
			for (Handler handler : handlers)
			{
				int result = handler.scan(sentence, offset, this, cache);
				if (result != offset)
				{
					offset = result;
					break;
				}
			}
		}
		if (cache.count() != 0)
		{
			Execution node = new Execution(cache.toString());
			stack.push(node);
		}
		Deque<Execution> array = new LinkedList<Execution>();
		while (stack.isEmpty() == false)
		{
			array.push(stack.pollFirst());
		}
		nodes = array.toArray(new Execution[0]);
		stack.clear();
	}
	
	private Syntax(Execution[] nodes)
	{
		this.nodes = nodes;
	}
	
	public String calculate(Map<String, Object> variables)
	{
		StringBuilder builder = new StringBuilder();
		for (Execution node : nodes)
		{
			Object value = node.calculate(variables);
			if (value != null)
			{
				builder.append(value);
			}
		}
		return builder.toString();
	}
	
	public Deque<Execution> getStack()
	{
		return stack;
	}
	
	public ScanMode getMode()
	{
		return mode;
	}
	
	public void setMode(ScanMode mode)
	{
		this.mode = mode;
	}
	
	public static Syntax parse(String sentence)
	{
		return new Syntax(sentence);
	}
	
	public static Syntax parse(Execution[] nodes)
	{
		return new Syntax(nodes);
	}
}
