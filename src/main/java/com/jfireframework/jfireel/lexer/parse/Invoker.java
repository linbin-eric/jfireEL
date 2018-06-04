package com.jfireframework.jfireel.lexer.parse;

import java.util.Deque;
import com.jfireframework.jfireel.lexer.node.CalculateNode;

public interface Invoker
{
	int parse(String el, int offset, Deque<CalculateNode> nodes, int function);
	
}
