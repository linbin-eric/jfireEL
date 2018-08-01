package com.jfireframework.jfireel.expression.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.node.impl.OperatorNode;
import com.jfireframework.jfireel.expression.parse.Invoker;
import com.jfireframework.jfireel.expression.token.Operator;
import com.jfireframework.jfireel.expression.util.CharType;

public class OperatorParser extends NodeParser
{
    
    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
    {
        if (CharType.isOperator(getChar(offset, el)) == false)
        {
            return next.parse(el, offset, nodes, function);
        }
        String literals = new String(new char[] { getChar(offset, el), getChar(offset + 1, el) });
        if (Operator.literalsOf(literals) != null)
        {
            nodes.push(new OperatorNode(Operator.literalsOf(literals)));
            offset += 2;
            return offset;
        }
        literals = String.valueOf(getChar(offset, el));
        if (Operator.literalsOf(literals) != null)
        {
            nodes.push(new OperatorNode(Operator.literalsOf(literals)));
            offset += 1;
            return offset;
        }
        throw new IllegalArgumentException("无法识别:" + literals + "检查:" + el.substring(0, offset));
    }
    
}
