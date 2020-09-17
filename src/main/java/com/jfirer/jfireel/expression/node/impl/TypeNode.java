package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.token.Token;

import java.util.Map;

public class TypeNode implements CalculateNode
{
    private final Class<?> ckass;
    private final Token    token;

    public TypeNode(Class<?> ckass, Token token)
    {
        this.ckass = ckass;
        this.token = token;
    }

    @Override
    public Object calculate(Map<String, Object> variables)
    {
        return ckass;
    }

    @Override
    public Token token()
    {
        return token;
    }

    @Override
    public String literals()
    {
        return ckass.getName();
    }

    public String toString()
    {
        return literals();
    }
}
