package com.jfireframework.jfireel.syntax.handler;

public enum ScanMode
{
    // 字符串
	LITERALS, //
    // 语言，主要指if，for
	LANGUAGE, //
    // 表达式 就是被${}包围起来的内容
	EXPRESSION, //
    // 代表的就是<%}%>中的}
	END,//
}
