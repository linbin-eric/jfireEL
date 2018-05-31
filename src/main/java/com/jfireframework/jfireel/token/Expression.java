package com.jfireframework.jfireel.token;

public enum Expression implements CalculateType
{
    // 参数变量
	VARIABLE, //
    // 属性访问
	PROPERTY, //
    // 方法访问，该节点尚未完全，需要填充参数。其本身不能作为参数值
	METHOD, //
    // 已经获取完毕参数的方法访问，该种节点可以作为参数值
	METHOD_RESULT, //
    // 字符串
	STRING, //
    // 数字
	NUMBER, //
    // 运算式结果
	OPERATOR_RESULT, //
    // 三元表达式
	QUESTION, //
    // []运算符
	BRACKET, //
    // Class类型
	TYPE, //
    // 是一个枚举类型的class
	TYPE_ENUM, //
    // 是一个枚举值
	ENUM,
    // 常量
	CONSTANT, //
	IF, //
	FOR,//
}
