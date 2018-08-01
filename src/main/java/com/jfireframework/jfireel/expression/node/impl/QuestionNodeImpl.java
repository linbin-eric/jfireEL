package com.jfireframework.jfireel.expression.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.node.QuestionNode;
import com.jfireframework.jfireel.expression.token.Token;
import com.jfireframework.jfireel.expression.token.TokenType;

public class QuestionNodeImpl implements QuestionNode
{
    private CalculateNode conditionNode;
    private CalculateNode expressionNode1;
    private CalculateNode expressionNode2;
    
    @Override
    public Object calculate(Map<String, Object> variables)
    {
        Object condition = conditionNode.calculate(variables);
        if (condition == null)
        {
            return null;
        }
        if ((Boolean) condition)
        {
            return expressionNode1.calculate(variables);
        }
        else
        {
            return expressionNode2.calculate(variables);
        }
    }
    
    @Override
    public TokenType type()
    {
        return Token.QUESTION;
    }
    
    @Override
    public void setConditionNode(CalculateNode node)
    {
        conditionNode = node;
    }
    
    @Override
    public void setLeftNode(CalculateNode node)
    {
        expressionNode1 = node;
    }
    
    @Override
    public void setRightNode(CalculateNode node)
    {
        expressionNode2 = node;
    }
    
    @Override
    public void check()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public String literals()
    {
        return conditionNode.literals() + "?" + expressionNode1.literals() + ":" + expressionNode2.literals();
    }
    
    @Override
    public String toString()
    {
        return literals();
    }
}
