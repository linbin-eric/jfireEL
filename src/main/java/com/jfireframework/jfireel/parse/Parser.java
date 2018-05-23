package com.jfireframework.jfireel.parse;

import java.util.Deque;
import com.jfireframework.jfireel.node.CalculateNode;

public interface Parser
{
	
	/**
	 * 在解析节点后返回新的偏移量
	 * 
	 * @param el
	 * @param offset
	 * @param nodes
	 * @return
	 */
	int parse(String el, int offset, Deque<CalculateNode> nodes, int function);
}
