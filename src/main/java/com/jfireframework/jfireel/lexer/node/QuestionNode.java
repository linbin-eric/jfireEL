package com.jfireframework.jfireel.lexer.node;

public interface QuestionNode extends CalculateNode
{
    void setConditionNode(CalculateNode node);
    
    void setLeftNode(CalculateNode node);
    
    void setRightNode(CalculateNode node);
}
