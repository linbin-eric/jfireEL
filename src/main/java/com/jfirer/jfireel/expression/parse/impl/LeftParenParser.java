package com.jfirer.jfireel.expression.parse.impl;

import java.util.Deque;
import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.node.impl.SymBolNode;
import com.jfirer.jfireel.expression.parse.Invoker;
import com.jfirer.jfireel.expression.token.Symbol;

public class LeftParenParser extends NodeParser
{
    
    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
    {
        if ('(' != getChar(offset, el))
        {
            return next.parse(el, offset, nodes, function);
        }
        offset += 1;
        nodes.push(new SymBolNode(Symbol.LEFT_PAREN));
        return offset;
    }
    
}
