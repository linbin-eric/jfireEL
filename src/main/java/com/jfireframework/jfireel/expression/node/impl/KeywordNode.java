package com.jfireframework.jfireel.expression.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.token.Token;
import com.jfireframework.jfireel.expression.token.TokenType;

public class KeywordNode implements CalculateNode
{
    private Object keywordValue;
    
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
        return Token.CONSTANT;
    }
    
    @Override
    public String toString()
    {
        return "KeywordNode [keywordValue=" + keywordValue + "]";
    }
    
    @Override
    public void check()
    {
        // TODO Auto-generated method stub
        
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
