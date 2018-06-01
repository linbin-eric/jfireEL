package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.TokenType;
import com.jfireframework.jfireel.lexer.token.Symbol;

public class SymBolNode implements CalculateNode
{
	
	private Symbol symbol;
	
	public SymBolNode(Symbol symbol)
	{
		this.symbol = symbol;
	}
	
	// 符号节点没有参数计算
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public TokenType type()
	{
		return symbol;
	}
	
	@Override
	public String toString()
	{
		return "SymBolNode [symbol=" + symbol + "]";
	}

	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
	
}
