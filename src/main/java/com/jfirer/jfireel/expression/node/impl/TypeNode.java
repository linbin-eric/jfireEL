package com.jfirer.jfireel.expression.node.impl;

import java.util.Map;
import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.token.Token;
import com.jfirer.jfireel.expression.token.TokenType;
import com.jfirer.jfireel.expression.token.ValueResult;

public class TypeNode implements CalculateNode
{
    private Class<?>  ckass;
    private TokenType type;
    
    public TypeNode(Class<?> ckass, TokenType type)
    {
        this.ckass = ckass;
        this.type = type;
    }
    
    @Override
    public Object calculate(Map<String, Object> variables)
    {
        return ckass;
    }
    
    @Override
    public TokenType type()
    {
        return type;
    }

    @Override
    public Token token()
    {
        return ValueResult.RESULT;
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
