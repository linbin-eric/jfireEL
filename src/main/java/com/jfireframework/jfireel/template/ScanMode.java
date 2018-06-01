package com.jfireframework.jfireel.template;

public enum ScanMode
{
    // 字符串
	LITERALS, //
    // 执行语句.说明当前处于<%%>包围之中
	EXECUTION, //
    // 表达式 就是被${}包围起来的内容
	EXPRESSION, //
}
