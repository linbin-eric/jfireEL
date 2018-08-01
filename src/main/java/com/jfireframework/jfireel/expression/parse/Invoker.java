package com.jfireframework.jfireel.expression.parse;

import java.util.Deque;
import com.jfireframework.jfireel.expression.node.CalculateNode;

public interface Invoker
{
    int parse(String el, int offset, Deque<CalculateNode> nodes, int function);
    
}
