package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.node.MethodNode;
import com.jfirer.jfireel.expression.token.TokenType;

public abstract class AbstractMethodNode implements MethodNode
{
    protected TokenType       type = TokenType.METHOD;
    protected CalculateNode[] argsNodes;

    @Override
    public void setArgsNodes(CalculateNode[] argsNodes)
    {
        this.argsNodes = argsNodes;
        type = TokenType.RESULT;
    }

    @Override
    public TokenType type()
    {
        return type;
    }
}
