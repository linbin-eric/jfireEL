package com.jfireframework.jfireel.syntax.handler;

import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.syntax.Syntax;

public interface Handler
{
	int scan(String sentence, int offset, Syntax syntax, StringCache cache);
}
