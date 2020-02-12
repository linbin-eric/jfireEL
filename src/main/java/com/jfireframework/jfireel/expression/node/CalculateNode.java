package com.jfireframework.jfireel.expression.node;

import com.jfireframework.jfireel.expression.token.TokenType;

import java.util.Map;

public interface CalculateNode
{

    Object calculate(Map<String, Object> variables);

    TokenType type();

    String literals();
}
