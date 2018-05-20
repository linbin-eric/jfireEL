package com.jfireframework.jfireel.parse;

import java.util.Deque;
import com.jfireframework.jfireel.node.CalculateNode;

public interface Parser
{
    /**
     * 返回该解析器是否匹配
     * 
     * @param el
     * @param offset
     * @param nodes
     * @return
     */
    boolean match(String el, int offset, Deque<CalculateNode> nodes, int function);
    
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
