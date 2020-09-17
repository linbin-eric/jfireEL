package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.node.MethodNode;
import com.jfirer.jfireel.expression.parse.Invoker;
import com.jfirer.jfireel.expression.token.Intermediate;
import com.jfirer.jfireel.expression.token.Symbol;
import com.jfirer.jfireel.expression.util.OperatorResultUtil;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class RightParenParser extends NodeParser
{

    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
    {
        if (')' != getChar(offset, el))
        {
            return next.parse(el, offset, nodes, function);
        }
        Deque<CalculateNode> deque = new LinkedList<CalculateNode>();
        CalculateNode        pred;
        while ((pred = nodes.pollFirst()) != null)
        {
            if (pred.token() != Intermediate.METHOD && pred.token() != Symbol.LEFT_PAREN)
            {
                deque.addFirst(pred);
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
        if (pred.token() == Intermediate.METHOD)
        {
            MethodNode          methodNode  = (MethodNode) pred;
            List<CalculateNode> argsNodes   = new LinkedList<CalculateNode>();
            int                 finalOffset = offset;
            LinkedList<CalculateNode> leftDeque = deque.stream().collect(() -> new LinkedList<CalculateNode>(), (tmpDeque, node) -> {
                if (node.token() == Symbol.COMMA)
                {
                    argsNodes.add(OperatorResultUtil.aggregate(tmpDeque, el, finalOffset));
                    tmpDeque.clear();
                }
                else
                {
                    tmpDeque.addLast(node);
                }
            }, (deque1, deque2) -> {});
            if (leftDeque.isEmpty() == false)
            {
                argsNodes.add(OperatorResultUtil.aggregate(leftDeque, el, offset));
            }
            methodNode.setArgsNodes(argsNodes.toArray(new CalculateNode[argsNodes.size()]));
            offset += 1;
            nodes.push(methodNode);
            return offset;
        }
        else
        {
            nodes.push(OperatorResultUtil.aggregate(((LinkedList) deque), el, offset));
            offset += 1;
            return offset;
        }
    }
}
