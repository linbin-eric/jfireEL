package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.node.MethodNode;
import com.jfirer.jfireel.expression.token.Intermediate;
import com.jfirer.jfireel.expression.token.Token;
import com.jfirer.jfireel.expression.token.ValueResult;

public abstract class AbstractMethodNode implements MethodNode
{
    protected CalculateNode[] argsNodes;
    protected Token           token = Intermediate.METHOD;

    @Override
    public void setArgsNodes(CalculateNode[] argsNodes)
    {
        this.argsNodes = argsNodes;
        token = ValueResult.METHOD;
    }

    @Override
    public Token token()
    {
        return token;
    }
}
