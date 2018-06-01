package com.jfireframework.jfireel.template;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.syntax.ScanMode;
import com.jfireframework.jfireel.template.exception.IllegalFormatException;
import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.execution.impl.StringExecution;
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
	private Deque<Execution>	executions	= new LinkedList<Execution>();
	private Execution[]			runtimeExecutions;
	private ScanMode			mode		= ScanMode.LITERALS;
	private Parser[]			parsers		= new Parser[] {				//
	        new ExecutionBeginParser(),										//
	        new ExecutionEndParser(),										//
	        new IfParser(),													//
	        new ElseParser(),												//
	        new ForEachParser(),											//
	        new EndBraceParser(),											//
	        new ExpressionParser(),											//
	        new LiteralsParser(),											//
	
	};
	
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
			int pred = offset;
			for (Parser parser : parsers)
			{
				int result = parser.scan(sentence, offset, executions, this, cache);
				if (result != offset)
				{
					offset = result;
					break;
				}
			}
			if (offset == pred)
			{
				throw new IllegalFormatException("没有解析器可以识别", sentence.substring(0, offset));
			}
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
		executions.clear();
	}
	
	private static final ThreadLocal<StringCache> LOCAL = new ThreadLocal<StringCache>() {
		protected StringCache initialValue()
		{
			return new StringCache();
		};
	};
	
	public String calculate(Map<String, Object> variables)
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
