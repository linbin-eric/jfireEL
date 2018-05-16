package com.jfireframework.jfireel.node;

import com.jfireframework.jfireel.node.impl.CompileMethodNode;
import com.jfireframework.jfireel.node.impl.DefaultMethodNode;
import com.jfireframework.jfireel.node.impl.EqualNode;
import com.jfireframework.jfireel.node.impl.KeywordNode;
import com.jfireframework.jfireel.node.impl.MinusNode;
import com.jfireframework.jfireel.node.impl.MutliNode;
import com.jfireframework.jfireel.node.impl.NotEqualNode;
import com.jfireframework.jfireel.node.impl.NumberNode;
import com.jfireframework.jfireel.node.impl.OperatorNode;
import com.jfireframework.jfireel.node.impl.OperatorResultNode;
import com.jfireframework.jfireel.node.impl.PlusNode;
import com.jfireframework.jfireel.node.impl.PropertyNode;
import com.jfireframework.jfireel.node.impl.StringNode;
import com.jfireframework.jfireel.node.impl.SymBolNode;
import com.jfireframework.jfireel.node.impl.VariableNode;
import com.jfireframework.jfireel.token.Operator;
import com.jfireframework.jfireel.token.Symbol;
import com.jfireframework.jfireel.util.Functions;

public class BuildInNodeFactory implements NodeFactory
{
	
	@Override
	public CalculateNode parseKeyWord(String literals, int function)
	{
		return new KeywordNode(literals);
	}
	
	@Override
	public CalculateNode parseVariable(String literals, int function)
	{
		return new VariableNode(literals);
	}
	
	@Override
	public CalculateNode parseSymBol(Symbol symbol, int function)
	{
		return new SymBolNode(symbol);
	}
	
	@Override
	public CalculateNode buildOperatorResultNode(CalculateNode leftNode, CalculateNode operatorNode, CalculateNode rightNode, int function)
	{
		OperatorResultNode resultNode = null;
		switch ((Operator) operatorNode.type())
		{
			case PLUS:
				resultNode = new PlusNode();
				break;
			case MULTI:
				resultNode = new MutliNode();
				break;
			case EQ:
				resultNode = new EqualNode();
				break;
			case NOT_EQ:
				resultNode = new NotEqualNode();
				break;
			case MINUS:
				resultNode = new MinusNode();
				break;
			default:
				throw new UnsupportedOperationException(((Operator) operatorNode.type()).toString());
		}
		resultNode.setLeftOperand(leftNode);
		resultNode.setRightOperand(rightNode);
		return resultNode;
	}
	
	@Override
	public CalculateNode buildMethodNode(CalculateNode beanNode, String literals, int function)
	{
		if (Functions.isMethodInvokeByCompile(function))
		{
			return new CompileMethodNode(literals, beanNode);
		}
		else
		{
			return new DefaultMethodNode(literals, beanNode);
		}
	}
	
	@Override
	public CalculateNode buildPropertyNode(CalculateNode beanNode, String literals, int function)
	{
		return new PropertyNode(literals, beanNode);
	}
	
	@Override
	public CalculateNode buildStringNode(String literals, int function)
	{
		return new StringNode(literals);
	}
	
	@Override
	public CalculateNode buildNumberNode(String literals, int function)
	{
		return new NumberNode(literals);
	}
	
	@Override
	public CalculateNode buildOperatorNode(Operator operator, int function)
	{
		return new OperatorNode(operator);
	}
	
}
