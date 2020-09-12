package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.token.Token;
import com.jfirer.jfireel.expression.token.TokenType;
import com.jfirer.jfireel.expression.token.ValueResult;

import java.util.Map;

public class StringNode implements CalculateNode
{
    private final String literals;

    public StringNode(String literals)
    {
        this.literals = literals;
    }

    @Override
    public Object calculate(Map<String, Object> variables)
    {
        return literals;
    }

    @Override
    public TokenType type()
    {
        return TokenType.STRING;
    }

    @Override
    public Token token()
    {
        return ValueResult.RESULT;
    }

    @Override
    public String literals()
    {
        return '\'' + literals + "'";
    }

    @Override
    public String toString()
    {
        return literals();
    }
}
