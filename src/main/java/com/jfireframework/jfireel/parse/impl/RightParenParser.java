package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.MethodNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.token.Expression;
import com.jfireframework.jfireel.token.Symbol;
import com.jfireframework.jfireel.util.CalculateNodeUtil;
import com.jfireframework.jfireel.util.CharType;

public class RightParenParser implements Parser
{
    
    @Override
    public boolean match(String el, int offset, Deque<CalculateNode> nodes, int function)
    {
        return ')' == CharType.getCurrentChar(offset, el);
    }
    
    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
    {
        List<CalculateNode> list = new LinkedList<CalculateNode>();
        CalculateNode pred;
        while ((pred = nodes.pollFirst()) != null)
        {
            if (pred.type() != Symbol.LEFT_PAREN && pred.type() != Expression.METHOD)
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
        if (pred.type() == Expression.METHOD)
        {
            MethodNode methodNode = (MethodNode) pred;
            List<CalculateNode> argsNodes = new LinkedList<CalculateNode>();
            for (int i = 0; i < list.size();)
            {
                if (list.get(i).type() == Symbol.COMMA)
                {
                    list.remove(i);
                    argsNodes.add(CalculateNodeUtil.aggregate(list.subList(0, i), function, el, offset));
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
                argsNodes.add(CalculateNodeUtil.aggregate(list, function, el, offset));
            }
            methodNode.setArgsNodes(argsNodes.toArray(new CalculateNode[argsNodes.size()]));
            offset += 1;
            nodes.push(methodNode);
            return offset;
        }
        else
        {
            nodes.push(CalculateNodeUtil.aggregate(list, function, el, offset));
            offset += 1;
            return offset;
        }
    }
}
