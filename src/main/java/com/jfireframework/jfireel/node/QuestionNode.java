package com.jfireframework.jfireel.node;

public interface QuestionNode extends CalculateNode
{
    void setConditionNode(CalculateNode node);
    
    void setLeftNode(CalculateNode node);
    
    void setRightNode(CalculateNode node);
}
