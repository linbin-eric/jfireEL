package com.jfireframework.jfireel.parse.impl;

import java.util.Deque;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.MethodNode;
import com.jfireframework.jfireel.node.impl.CompileMethodNode;
import com.jfireframework.jfireel.node.impl.DefaultMethodNode;
import com.jfireframework.jfireel.parse.Parser;
import com.jfireframework.jfireel.util.CharType;
import com.jfireframework.jfireel.util.Functions;

public class MethodParser implements Parser
{
    
    @Override
    public boolean match(String el, int offset, Deque<CalculateNode> nodes, int function)
    {
        if ('.' != CharType.getCurrentChar(offset, el))
        {
            return false;
        }
        offset += 1;
        char c;
        while (CharType.isAlphabet(c = CharType.getCurrentChar(offset, el)) || CharType.isDigital(c))
        {
            offset++;
        }
        // 该情况意味着是方法
        if (c == '(')
        {
            return true;
        }
        // 不是方法，则必然是属性
        else
        {
            return false;
        }
    }
    
    @Override
    public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
    {
        int origin = offset;
        offset += 1;
        char c;
        while (CharType.isAlphabet(c = CharType.getCurrentChar(offset, el)) || CharType.isDigital(c))
        {
            offset++;
        }
        String literals = el.substring(origin, offset);
        CalculateNode beanNode = nodes.pop();
        MethodNode methodNode;
        if (Functions.isMethodInvokeByCompile(function))
        {
            methodNode = new CompileMethodNode(literals, beanNode);
        }
        else
        {
            methodNode = new DefaultMethodNode(literals, beanNode);
        }
        nodes.push(methodNode);
        // 当前位置是(，所以位置+1
        offset += 1;
        return offset;
    }
    
}
