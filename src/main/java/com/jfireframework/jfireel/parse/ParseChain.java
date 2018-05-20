package com.jfireframework.jfireel.parse;

import java.util.Deque;
import com.jfireframework.jfireel.node.CalculateNode;

public class ParseChain
{
    private Parser[] parsers;
    
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
    {
        for (Parser each : parsers)
        {
            if (each.match(el, offset, nodes, function))
            {
                offset = each.parse(el, offset, nodes, function);
            }
        }
        return offset;
    }
    
    public void setParsers(Parser... parsers)
    {
        this.parsers = parsers;
    }
}
