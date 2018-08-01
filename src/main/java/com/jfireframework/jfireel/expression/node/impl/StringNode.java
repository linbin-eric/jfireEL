package com.jfireframework.jfireel.expression.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.token.Token;
import com.jfireframework.jfireel.expression.token.TokenType;

public class StringNode implements CalculateNode
{
    private String literals;
    
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
        return Token.STRING;
    }
    
    @Override
    public void check()
    {
        
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
