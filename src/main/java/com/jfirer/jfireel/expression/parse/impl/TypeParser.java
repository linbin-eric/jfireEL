package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.node.impl.TypeNode;
import com.jfirer.jfireel.expression.parse.Invoker;
import com.jfirer.jfireel.expression.token.TokenType;
import com.jfirer.jfireel.expression.util.CharType;

import java.util.Deque;

public class TypeParser extends NodeParser
{

    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
    {
        if ('T' != getChar(offset, el) || '(' != getChar(offset + 1, el))
        {
            return next.parse(el, offset, nodes, function);
        }
        offset += 2;
        offset = skipWhiteSpace(offset, el);
        int  origin = offset;
        char c;
        while (CharType.isAlphabet(c = getChar(offset, el)) || '.' == c || '_' == c || '$' == c)
        {
            offset++;
        }
        int end = offset;
        offset = skipWhiteSpace(offset, el);
        if (')' != getChar(offset, el))
        {
            throw new IllegalArgumentException("类型操作没有被)包围，检查:" + el.substring(origin, offset));
        }
        String literals = el.substring(origin, end);
        try
        {
            Class<?> type = Class.forName(literals);
            if (Enum.class.isAssignableFrom(type))
            {
                nodes.push(new TypeNode(type, TokenType.TYPE_ENUM));
            }
            else
            {
                nodes.push(new TypeNode(type, TokenType.TYPE));
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        offset += 1;
        return offset;
    }
}
