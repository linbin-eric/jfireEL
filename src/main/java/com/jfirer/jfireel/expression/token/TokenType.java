package com.jfirer.jfireel.expression.token;

public enum TokenType
{
    // 参数变量
    VARIABLE, //
    // 属性访问
    PROPERTY, //
    // 方法访问
    METHOD, //
    // 字符串
    STRING, //
    // 数字
    NUMBER, //
    // 符号
    SYMBOL,//
    // 运算符
    OPERATOR,//
    // 三元表达式
    QUESTION, //
    // []运算符
    // Class类型
    TYPE, //
    // 是一个枚举类型的class
    TYPE_ENUM, //
    // 是一个枚举值
    ENUM, // 常量
    CONSTANT, //
    //结果标记，意味着该token已经处理完毕，不需要其他处理了
    RESULT, IF, //
    ELSE, //
    ELSE_IF, //
    FOR,//
}
