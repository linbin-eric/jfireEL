package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.token.Operator;
import com.jfirer.jfireel.expression.token.Token;
import com.jfirer.jfireel.expression.token.TokenType;

import java.util.Map;

public class OperatorNode implements CalculateNode
{
    private final Operator operatorType;

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
        return TokenType.OPERATOR;
    }

    @Override
    public Token token()
    {
        return operatorType;
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
