package com.jfireframework.jfireel.node;

import java.util.Map;
import com.jfireframework.jfireel.CalculateType;
import com.jfireframework.jfireel.Symbol;

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
    public CalculateType type()
    {
        return symbol;
    }
    
}
