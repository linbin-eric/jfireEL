package com.jfireframework.jfireel.expression.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.node.impl.NumberNode;
import com.jfireframework.jfireel.expression.parse.Invoker;
import com.jfireframework.jfireel.expression.token.Operator;
import com.jfireframework.jfireel.expression.util.CharType;

public class NumberParser extends NodeParser
{
    
    private boolean match(String el, int offset, Deque<CalculateNode> nodes, int function)
    {
        if ('-' == getChar(offset, el))
        {
            // 这种情况下，-是一个操作符
            if (nodes.peek() != null && nodes.peek().type() instanceof Operator == false)
            {
                return false;
            }
            // 这种情况下，-代表是一个负数
            if (CharType.isDigital(getChar(offset + 1, el)))
            {
                return true;
            }
            else
            {
                throw new IllegalArgumentException("无法识别的-符号，不是负数也不是操作符,问题区间:" + el.substring(0, offset));
            }
        }
        else if (CharType.isDigital(getChar(offset, el)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
    {
        if (match(el, offset, nodes, function) == false)
        {
            return next.parse(el, offset, nodes, function);
        }
        int index = offset;
        char c = getChar(offset, el);
        if (c == '-')
        {
            offset += 1;
        }
        boolean hasDot = false;
        while (CharType.isDigital(c = getChar(offset, el)) || (hasDot == false && c == '.'))
        {
            offset++;
            if (c == '.')
            {
                hasDot = true;
            }
        }
        if (c == '.')
        {
            throw new IllegalArgumentException("非法的负数格式,问题区间:" + el.substring(index, offset));
        }
        String literals = el.substring(index, offset);
        nodes.push(new NumberNode(literals));
        return offset;
    }
    
}
