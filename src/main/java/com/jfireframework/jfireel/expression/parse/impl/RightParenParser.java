package com.jfireframework.jfireel.expression.parse.impl;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.node.MethodNode;
import com.jfireframework.jfireel.expression.parse.Invoker;
import com.jfireframework.jfireel.expression.token.Symbol;
import com.jfireframework.jfireel.expression.token.Token;
import com.jfireframework.jfireel.expression.util.OperatorResultUtil;

public class RightParenParser extends NodeParser
{
    
    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
    {
        if (')' != getChar(offset, el))
        {
            return next.parse(el, offset, nodes, function);
        }
        List<CalculateNode> list = new LinkedList<CalculateNode>();
        CalculateNode pred;
        while ((pred = nodes.pollFirst()) != null)
        {
            if (pred.type() != Symbol.LEFT_PAREN && pred.type() != Token.METHOD)
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
        if (pred.type() == Token.METHOD)
        {
            MethodNode methodNode = (MethodNode) pred;
            List<CalculateNode> argsNodes = new LinkedList<CalculateNode>();
            for (int i = 0; i < list.size();)
            {
                if (list.get(i).type() == Symbol.COMMA)
                {
                    list.remove(i);
                    argsNodes.add(OperatorResultUtil.aggregate(list.subList(0, i), function, el, offset));
                    list.remove(0);
                    i = 0;
                }
                else
                {
                    i++;
                }
            }
            if (list.isEmpty() == false)
            {
                argsNodes.add(OperatorResultUtil.aggregate(list, function, el, offset));
            }
            methodNode.setArgsNodes(argsNodes.toArray(new CalculateNode[argsNodes.size()]));
            offset += 1;
            nodes.push(methodNode);
            return offset;
        }
        else
        {
            nodes.push(OperatorResultUtil.aggregate(list, function, el, offset));
            offset += 1;
            return offset;
        }
    }
}
