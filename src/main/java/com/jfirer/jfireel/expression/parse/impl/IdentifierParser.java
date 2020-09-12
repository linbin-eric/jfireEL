package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.node.impl.KeywordNode;
import com.jfirer.jfireel.expression.node.impl.VariableNode;
import com.jfirer.jfireel.expression.parse.Invoker;
import com.jfirer.jfireel.expression.token.KeyWord;
import com.jfirer.jfireel.expression.util.CharType;

import java.util.Deque;

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
        int  length = 0;
        char c;
        while (CharType.isAlphabet(c = getChar(length + offset, el)) || CharType.isDigital(c))
        {
            length++;
        }
        String literals = el.substring(offset, offset + length);
        offset += length;
        if (KeyWord.getKeyWord(literals) != null)
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
