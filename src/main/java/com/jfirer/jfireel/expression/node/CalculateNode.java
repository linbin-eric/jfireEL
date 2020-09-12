package com.jfirer.jfireel.expression.node;

import com.jfirer.jfireel.expression.token.Token;
import com.jfirer.jfireel.expression.token.TokenType;

import java.util.Map;

public interface CalculateNode
{

    Object calculate(Map<String, Object> variables);

    TokenType type();

    Token token();

    String literals();
}
