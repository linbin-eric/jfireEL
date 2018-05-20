package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.impl.SymBolNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.token.Symbol;
import com.jfireframework.jfireel.util.CharType;

public class CommaParser implements Parser
{
    
    @Override
    public boolean match(String el, int offset, Deque<CalculateNode> nodes, int function)
    {
        return ',' == CharType.getCurrentChar(offset, el);
    }
    
    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
    {
        nodes.push(new SymBolNode(Symbol.COMMA));
        offset += 1;
        return offset;
    }
    
}
