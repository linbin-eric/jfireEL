package com.jfireframework.jfireel.expression.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.token.Symbol;
import com.jfireframework.jfireel.expression.token.TokenType;

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
    public void check()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public String literals()
    {
        return symbol.getLiterals();
    }
    
}
