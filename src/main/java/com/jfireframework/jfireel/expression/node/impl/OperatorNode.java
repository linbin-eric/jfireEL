package com.jfireframework.jfireel.expression.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.token.Operator;
import com.jfireframework.jfireel.expression.token.TokenType;

public class OperatorNode implements CalculateNode
{
    private Operator operatorType;
    
    public OperatorNode(Operator operatorType)
    {
        this.operatorType = operatorType;
    }
    
    // 操作符节点不会有计算动作
    @Override
    public Object calculate(Map<String, Object> variables)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public TokenType type()
    {
        return operatorType;
    }
    
    @Override
    public void check()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public String literals()
    {
        return operatorType.getLiterals();
    }
    
    @Override
    public String toString()
    {
        return literals();
    }
}
