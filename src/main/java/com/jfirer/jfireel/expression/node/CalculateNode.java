package com.jfirer.jfireel.expression.node;

import com.jfirer.jfireel.expression.token.Token;

import java.util.Map;

public interface CalculateNode
{

    Object calculate(Map<String, Object> variables);

    Token token();

    String literals();
}
