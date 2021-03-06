package com.jfireframework.jfireel.expression.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.token.Token;
import com.jfireframework.jfireel.expression.token.TokenType;

public class EnumNode implements CalculateNode
{
    private final Enum<?> value;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
        return Token.ENUM;
    }
    
    @Override
    public void check()
    {
        // TODO Auto-generated method stub
        
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
