package com.jfireframework.jfireel.expression.node.impl;

import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.token.Operator;
import com.jfireframework.jfireel.expression.token.Token;
import com.jfireframework.jfireel.expression.token.TokenType;

public abstract class OperatorResultNode implements CalculateNode
{
    protected CalculateNode leftOperand;
    protected CalculateNode rightOperand;
    protected Operator      type;
    
    protected OperatorResultNode(Operator type)
    {
        this.type = type;
    }
    
    public void setLeftOperand(CalculateNode node)
    {
        leftOperand = node;
    }
    
    public void setRightOperand(CalculateNode node)
    {
        rightOperand = node;
    }
    
    @Override
    public TokenType type()
    {
        return Token.OPERATOR_RESULT;
    }
    
    public String literals()
    {
        return leftOperand.literals() + type.getLiterals() + rightOperand.literals();
    }
    
    @Override
    public String toString()
    {
        return literals();
    }
}
