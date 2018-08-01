package com.jfireframework.jfireel.expression.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.node.impl.SymBolNode;
import com.jfireframework.jfireel.expression.parse.Invoker;
import com.jfireframework.jfireel.expression.token.Symbol;

public class LeftBracketParser extends NodeParser
{
    
    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
    {
        if ('[' != getChar(offset, el))
        {
            return next.parse(el, offset, nodes, function);
        }
        nodes.push(new SymBolNode(Symbol.LEFT_BRACKET));
        offset += 1;
        return offset;
    }
    
}
