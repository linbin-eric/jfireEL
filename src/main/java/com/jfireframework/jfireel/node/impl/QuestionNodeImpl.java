package com.jfireframework.jfireel.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.QuestionNode;
import com.jfireframework.jfireel.token.CalculateType;
import com.jfireframework.jfireel.token.Expression;

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
    public CalculateType type()
    {
        return Expression.QUESTION;
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
    
}
