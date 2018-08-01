package com.jfireframework.jfireel.expression.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.exception.IllegalFormatException;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.node.impl.StringNode;
import com.jfireframework.jfireel.expression.parse.Invoker;

public class ConstantStringParser extends NodeParser
{
    
    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
    {
        if ('\'' != getChar(offset, el))
        {
            return next.parse(el, offset, nodes, function);
        }
        offset += 1;
        int origin = offset;
        int length = el.length();
        while (offset < length && getChar(offset, el) != '\'')
        {
            offset++;
        }
        if (getChar(offset, el) != '\'')
        {
            throw new IllegalFormatException("字符串表达式没有被'包围", el.substring(origin - 1));
        }
        String literals = el.substring(origin, offset);
        nodes.push(new StringNode(literals));
        offset += 1;
        return offset;
    }
    
}
