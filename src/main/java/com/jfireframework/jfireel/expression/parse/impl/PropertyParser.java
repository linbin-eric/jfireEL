package com.jfireframework.jfireel.expression.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.node.impl.DynamicReflectPropertyNode;
import com.jfireframework.jfireel.expression.node.impl.DynamicUnsafePropertyNode;
import com.jfireframework.jfireel.expression.node.impl.StaticPropertyNode;
import com.jfireframework.jfireel.expression.parse.Invoker;
import com.jfireframework.jfireel.expression.token.Token;
import com.jfireframework.jfireel.expression.util.CharType;
import com.jfireframework.jfireel.expression.util.Functions;

public class PropertyParser extends NodeParser
{
    
    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function, Invoker next)
    {
        // 如果是后一种情况，意味着此时应该是一个枚举值而不是属性
        if ('.' != getChar(offset, el)//
                || (nodes.peek() != null && nodes.peek().type() == Token.TYPE_ENUM))
        {
            return next.parse(el, offset, nodes, function);
        }
        int origin = offset;
        offset += 1;
        char c;
        while (CharType.isAlphabet(c = getChar(offset, el)) || CharType.isDigital(c))
        {
            offset++;
        }
        // 该情况意味着是方法
        if (c == '(')
        {
            return next.parse(el, origin, nodes, function);
        }
        String literals = el.substring(origin + 1, offset);
        CalculateNode beanNode = nodes.pop();
        CalculateNode current;
        if (beanNode.type() == Token.TYPE)
        {
            current = new StaticPropertyNode(literals, beanNode);
        }
        else
        {
            if (Functions.isPropertyFetchByUnsafe(function))
            {
                current = new DynamicUnsafePropertyNode(literals, beanNode, Functions.isRecognizeEveryTime(function));
            }
            else if (Functions.isPropertyFetchByReflect(function))
            {
                current = new DynamicReflectPropertyNode(literals, beanNode, Functions.isRecognizeEveryTime(function));
            }
            else
            {
                current = new DynamicReflectPropertyNode(literals, beanNode, Functions.isRecognizeEveryTime(function));
            }
        }
        nodes.push(current);
        return offset;
    }
    
}
