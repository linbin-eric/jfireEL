package com.jfireframework.jfireel.syntax;

public enum ScanMode
{
    // 字符串
	LITERALS, //
    // 执行语句
	EXECUTION, //
    // 表达式 就是被${}包围起来的内容
	EXPRESSION, //
    // 代表的就是<%}%>中的}
	END,//
}
