package com.jfirer.jfireel.expression.node;

public interface QuestionNode extends CalculateNode
{
    void setConditionNode(CalculateNode node);

    void setLeftNode(CalculateNode node);

    void setRightNode(CalculateNode node);
}
