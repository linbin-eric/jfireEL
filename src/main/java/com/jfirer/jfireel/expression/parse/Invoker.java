package com.jfirer.jfireel.expression.parse;

import com.jfirer.jfireel.expression.node.CalculateNode;

import java.util.Deque;

public interface Invoker
{
    int parse(String el, int offset, Deque<CalculateNode> nodes, int function);
}
