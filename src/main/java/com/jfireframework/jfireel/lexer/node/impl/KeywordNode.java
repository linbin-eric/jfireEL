package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;

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
    public CalculateType type()
    {
        return Expression.CONSTANT;
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
    
}
