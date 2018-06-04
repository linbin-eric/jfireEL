package com.jfireframework.jfireel.template;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.template.exception.IllegalFormatException;
import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.execution.impl.StringExecution;
import com.jfireframework.jfireel.template.parser.Invoker;
import com.jfireframework.jfireel.template.parser.Parser;
import com.jfireframework.jfireel.template.parser.impl.ElseParser;
import com.jfireframework.jfireel.template.parser.impl.EndBraceParser;
import com.jfireframework.jfireel.template.parser.impl.ExecutionBeginParser;
import com.jfireframework.jfireel.template.parser.impl.ExecutionEndParser;
import com.jfireframework.jfireel.template.parser.impl.ExpressionParser;
import com.jfireframework.jfireel.template.parser.impl.ForEachParser;
import com.jfireframework.jfireel.template.parser.impl.IfParser;
import com.jfireframework.jfireel.template.parser.impl.LiteralsParser;

public class Template
{
	private static final ThreadLocal<StringCache>	LOCAL		= new ThreadLocal<StringCache>() {
																	@Override
																	protected StringCache initialValue()
																	{
																		return new StringCache();
																	};
																};
	private Deque<Execution>						executions	= new LinkedList<Execution>();
	private Execution[]								runtimeExecutions;
	private ScanMode								mode		= ScanMode.LITERALS;
	private Invoker									head		= DEFAULT_HEAD;
	private static final Invoker					DEFAULT_HEAD;
	static
	{
		Parser[] parsers = new Parser[] { //
		        new ExecutionBeginParser(), //
		        new ExecutionEndParser(), //
		        new IfParser(), //
		        new ElseParser(), //
		        new ForEachParser(), //
		        new EndBraceParser(), //
		        new ExpressionParser(), //
		        new LiteralsParser(), //
		
		};
		Invoker pred = new Invoker() {
			
			@Override
			public int scan(String sentence, int offset, Deque<Execution> executions, Template template, StringCache cache)
			{
				return offset;
			}
		};
		for (int i = parsers.length - 1; i > -1; i--)
		{
			final Parser parser = parsers[i];
			final Invoker next = pred;
			Invoker invoker = new Invoker() {
				
				@Override
				public int scan(String sentence, int offset, Deque<Execution> executions, Template template, StringCache cache)
				{
					return parser.parse(sentence, offset, executions, template, cache, next);
				}
			};
			pred = invoker;
		}
		DEFAULT_HEAD = pred;
	}
	
	public ScanMode getMode()
	{
		return mode;
	}
	
	public void setMode(ScanMode mode)
	{
		this.mode = mode;
	}
	
	private Template(String sentence)
	{
		StringCache cache = new StringCache();
		int offset = 0;
		int length = sentence.length();
		mode = ScanMode.LITERALS;
		while (offset < length)
		{
			int result = head.scan(sentence, offset, executions, this, cache);
			if (result == offset)
			{
				throw new IllegalFormatException("没有解析器可以识别", sentence.substring(0, offset));
			}
			offset = result;
		}
		if (cache.count() != 0)
		{
			Execution execution = new StringExecution(cache.toString());
			executions.push(execution);
		}
		Deque<Execution> array = new LinkedList<Execution>();
		while (executions.isEmpty() == false)
		{
			array.push(executions.pollFirst());
		}
		runtimeExecutions = array.toArray(new Execution[0]);
		executions = null;
		mode = null;
	}
	
	public String render(Map<String, Object> variables)
	{
		StringCache cache = LOCAL.get();
		for (Execution execution : runtimeExecutions)
		{
			execution.execute(variables, cache);
		}
		String result = cache.toString();
		cache.clear();
		return result;
	}
	
	public static Template parse(String sentence)
	{
		return new Template(sentence);
	}
}
