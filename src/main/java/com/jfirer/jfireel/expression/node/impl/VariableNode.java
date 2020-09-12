package com.jfirer.jfireel.expression.node.impl;

import java.util.Map;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.token.Token;
import com.jfirer.jfireel.expression.token.TokenType;
import com.jfirer.jfireel.expression.token.ValueResult;

public class VariableNode implements CalculateNode
{
    private String literals;

    public VariableNode(String literals)
    {
        this.literals = literals;
    }

    @Override
    public Object calculate(Map<String, Object> variables)
    {
        return variables.get(literals);
    }

    @Override
    public TokenType type()
    {
        return TokenType.VARIABLE;
    }

    @Override
    public Token token()
    {
        return ValueResult.VARIABLE;
    }

    @Override
    public String toString()
    {
        return "VariableNode [literals=" + literals + "]";
    }

    @Override
    public String literals()
    {
        return literals;
    }
}
