package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.token.Symbol;
import com.jfirer.jfireel.expression.token.Token;

import java.util.Map;

public class SymBolNode implements CalculateNode
{

    private final Symbol symbol;

    public SymBolNode(Symbol symbol)
    {
        this.symbol = symbol;
    }

    // 符号节点没有参数计算
    @Override
    public Object calculate(Map<String, Object> variables)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Token token()
    {
        return symbol;
    }

    @Override
    public String toString()
    {
        return literals();
    }

    @Override
    public String literals()
    {
        return symbol.getLiterals();
    }
}
