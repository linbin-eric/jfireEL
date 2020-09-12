package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.token.Token;
import com.jfirer.jfireel.expression.token.TokenType;
import com.jfirer.jfireel.expression.token.ValueResult;

import java.util.Map;

public class KeywordNode implements CalculateNode
{
    private final Object keywordValue;

    public KeywordNode(String literals)
    {
        if (literals.equalsIgnoreCase("true"))
        {
            keywordValue = Boolean.TRUE;
        }
        else if (literals.equalsIgnoreCase("false"))
        {
            keywordValue = Boolean.FALSE;
        }
        else if (literals.equalsIgnoreCase("null"))
        {
            keywordValue = null;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Object calculate(Map<String, Object> variables)
    {
        return keywordValue;
    }

    @Override
    public TokenType type()
    {
        return TokenType.CONSTANT;
    }

    @Override
    public Token token()
    {
        return ValueResult.RESULT;
    }

    @Override
    public String toString()
    {
        return "KeywordNode [keywordValue=" + keywordValue + "]";
    }

    @Override
    public String literals()
    {
        if (keywordValue != null)
        {
            return keywordValue.toString();
        }
        else
        {
            return "NULL";
        }
    }
}
