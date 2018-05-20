package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.util.CharType;

public class SkipIgnoredToken implements Parser
{
    
    @Override
    public boolean match(String el, int offset, Deque<CalculateNode> nodes, int function)
    {
        return true;
    }
    
    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
    {
        while (CharType.isWhitespace(CharType.getCurrentChar(offset, el)))
        {
            offset++;
        }
        return offset;
    }
    
}
