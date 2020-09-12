package com.jfirer.jfireel.expression.parse.impl;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.node.impl.BracketNode;
import com.jfirer.jfireel.expression.parse.Invoker;
import com.jfirer.jfireel.expression.token.TokenType;
import com.jfirer.jfireel.expression.util.OperatorResultUtil;

public class RightBracketParser extends NodeParser
{

    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
    {
        if (']' != getChar(offset, el))
        {
            return next.parse(el, offset, nodes, function);
        }
        List<CalculateNode> list = new LinkedList<CalculateNode>();
        CalculateNode       pred;
        while ((pred = nodes.pollFirst()) != null)
        {
            if (pred.type() != TokenType.SYMBOL)
            {
                list.add(0, pred);
            }
            else
            {
                break;
            }
        }
        if (pred == null)
        {
            throw new IllegalArgumentException(el.substring(0, offset));
        }
        CalculateNode valueNode = OperatorResultUtil.aggregate(list, el, offset);
        CalculateNode beanNode  = nodes.pollFirst();
        if (beanNode == null)
        {
            throw new UnsupportedOperationException();
        }
        nodes.push(new BracketNode(beanNode, valueNode));
        offset += 1;
        return offset;
    }
}
