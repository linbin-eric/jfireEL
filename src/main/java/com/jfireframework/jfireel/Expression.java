package com.jfireframework.jfireel;

public enum Expression implements CalculateType
{
    // 参数变量
	VARIABLE, //
    // 属性访问
	PROPERTY, //
    // 方法访问
	METHOD, //
    // 字符串
	STRING, //
    // 常量
	CONSTANT, //
}
