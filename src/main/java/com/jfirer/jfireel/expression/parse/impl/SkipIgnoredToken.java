package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.parse.Invoker;

import java.util.Deque;

public class SkipIgnoredToken extends NodeParser
{

    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
    {
        offset = skipWhiteSpace(offset, el);
        return next.parse(el, offset, nodes, function);
    }
}
