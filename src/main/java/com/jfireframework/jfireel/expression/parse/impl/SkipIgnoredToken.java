package com.jfireframework.jfireel.expression.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.parse.Invoker;

public class SkipIgnoredToken extends NodeParser
{
    
    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
    {
        offset = skipWhiteSpace(offset, el);
        return next.parse(el, offset, nodes, function);
    }
    
}
