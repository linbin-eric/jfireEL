package com.jfireframework.jfireel.lexer.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.parse.Parser;
import com.jfireframework.jfireel.lexer.util.CharType;

public class SkipIgnoredToken implements Parser
{
    
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
