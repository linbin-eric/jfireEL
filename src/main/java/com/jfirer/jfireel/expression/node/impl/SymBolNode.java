package com.jfirer.jfireel.expression.node.impl;

import java.util.Map;
import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.token.Symbol;
import com.jfirer.jfireel.expression.token.TokenType;

public class SymBolNode implements CalculateNode
{
    
    private Symbol symbol;
    
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
    public TokenType type()
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
