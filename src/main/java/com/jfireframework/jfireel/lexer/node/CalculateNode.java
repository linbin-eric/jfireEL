package com.jfireframework.jfireel.lexer.node;

import java.util.Map;
import com.jfireframework.jfireel.lexer.token.TokenType;

public interface CalculateNode
{
	
	Object calculate(Map<String, Object> variables);
	
	TokenType type();
	
	/**
	 * 检查该节点是否完善
	 */
	void check();
	
	String literals();
}
