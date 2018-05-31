package com.jfireframework.jfireel.syntax.handler.impl;

import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.lexer.Lexer;
import com.jfireframework.jfireel.syntax.Syntax;
import com.jfireframework.jfireel.syntax.handler.Execution;
import com.jfireframework.jfireel.syntax.handler.ScanMode;

public class LanguageEndHandler extends AbstractHandler
{
	
	@Override
	public int scan(String sentence, int offset, Syntax syntax, StringCache cache)
	{
		if (syntax.getMode() != ScanMode.LANGUAGE && syntax.getMode() != ScanMode.END)
		{
			return offset;
		}
		char c = getCurrent(offset, sentence);
		if (c != '%')
		{
			return offset;
		}
		c = getCurrent(offset + 1, sentence);
		if (c != '>')
		{
			return offset;
		}
		ScanMode mode = syntax.getMode();
		if (mode == ScanMode.END)
		{
			syntax.setMode(ScanMode.LITERALS);
			offset += 2;
			return offset;
		}
		else if (mode == ScanMode.LANGUAGE)
		{
			syntax.setMode(ScanMode.LITERALS);
			Execution execution = new Execution(Lexer.parse(cache.toString()), Execution.LANGUAGE);
			cache.clear();
			syntax.getStack().push(execution);
			offset += 2;
			return offset;
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
}
