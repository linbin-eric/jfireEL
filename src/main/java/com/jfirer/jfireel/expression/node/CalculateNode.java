package com.jfirer.jfireel.expression.node;

import com.jfirer.jfireel.expression.token.TokenType;

import java.util.Map;

public interface CalculateNode
{

    Object calculate(Map<String, Object> variables);

    TokenType type();

    String literals();
}
