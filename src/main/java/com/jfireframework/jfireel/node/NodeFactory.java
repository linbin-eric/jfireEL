package com.jfireframework.jfireel.node;

import com.jfireframework.jfireel.token.Operator;
import com.jfireframework.jfireel.token.Symbol;

public interface NodeFactory
{
	CalculateNode parseKeyWord(String literals, int function);
	
	CalculateNode parseVariable(String literals, int function);
	
	CalculateNode parseSymBol(Symbol symbol, int function);
	
	CalculateNode buildOperatorResultNode(CalculateNode leftNode, CalculateNode operatorNode, CalculateNode rightNode, int function);
	
	CalculateNode buildMethodNode(CalculateNode beanNode, String literals, int function);
	
	CalculateNode buildPropertyNode(CalculateNode beanNode, String literals, int function);
	
	CalculateNode buildStringNode(String literals, int function);
	
	CalculateNode buildNumberNode(String literals, int function);
	
	CalculateNode buildOperatorNode(Operator operator, int function);
}
