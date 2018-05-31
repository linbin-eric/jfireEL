package com.jfireframework.jfireel.syntax.parser;

import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.syntax.Syntax;

public interface Parser
{
	int scan(String sentence, int offset, Syntax syntax, StringCache cache);
}
