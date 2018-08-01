package com.jfireframework.jfireel.expression.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.token.Token;
import com.jfireframework.jfireel.expression.token.TokenType;

public class TypeNode implements CalculateNode
{
    private Class<?> ckass;
    private Token    type;
    
    public TypeNode(Class<?> ckass, Token type)
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
    public void check()
    {
        // TODO Auto-generated method stub
        
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
