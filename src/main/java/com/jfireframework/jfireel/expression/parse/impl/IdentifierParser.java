package com.jfireframework.jfireel.expression.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.node.impl.KeywordNode;
import com.jfireframework.jfireel.expression.node.impl.VariableNode;
import com.jfireframework.jfireel.expression.parse.Invoker;
import com.jfireframework.jfireel.expression.token.DefaultKeyWord;
import com.jfireframework.jfireel.expression.util.CharType;

public class IdentifierParser extends NodeParser
{
    
    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
    {
        if (CharType.isAlphabet(getChar(offset, el)) == false)
        {
            return next.parse(el, offset, nodes, function);
        }
        return parseIdentifier(el, offset, nodes);
    }
    
    private int parseIdentifier(String el, int offset, Deque<CalculateNode> nodes)
    {
        int length = 0;
        char c;
        while (CharType.isAlphabet(c = getChar(length + offset, el)) || CharType.isDigital(c))
        {
            length++;
        }
        String literals = el.substring(offset, offset + length);
        offset += length;
        if (DefaultKeyWord.getDefaultKeyWord(literals) != null)
        {
            nodes.push(new KeywordNode(literals));
        }
        else
        {
            nodes.push(new VariableNode(literals));
        }
        return offset;
    }
    
}
