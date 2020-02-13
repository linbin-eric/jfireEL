package com.jfirer.jfireel.expression.parse;

import java.util.Deque;
import com.jfirer.jfireel.expression.node.CalculateNode;

public interface Invoker
{
    int parse(String el, int offset, Deque<CalculateNode> nodes, int function);
    
}
