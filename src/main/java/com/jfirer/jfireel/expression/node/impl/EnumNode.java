package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.token.Token;
import com.jfirer.jfireel.expression.token.TokenType;
import com.jfirer.jfireel.expression.token.ValueResult;

import java.util.Map;

public class EnumNode implements CalculateNode
{
    private final Enum<?> value;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public EnumNode(CalculateNode enumTypeNode, String literals)
    {
        Class<Enum> enumType = (Class<Enum>) enumTypeNode.calculate(null);
        value = Enum.valueOf(enumType, literals);
    }

    @Override
    public Object calculate(Map<String, Object> variables)
    {
        return value;
    }

    @Override
    public TokenType type()
    {
        return TokenType.ENUM;
    }

    @Override
    public Token token()
    {
        return ValueResult.RESULT;
    }

    @Override
    public String literals()
    {
        return value.name();
    }

    @Override
    public String toString()
    {
        return literals();
    }
}
