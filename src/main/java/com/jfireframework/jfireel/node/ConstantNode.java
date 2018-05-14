package com.jfireframework.jfireel.node;

import java.util.Map;
import com.jfireframework.jfireel.CalculateType;
import com.jfireframework.jfireel.CharType;
import com.jfireframework.jfireel.Expression;

public class ConstantNode implements CalculateNode
{
	private final Object constantValue;
	
	public ConstantNode(String literals)
	{
		if (literals.equalsIgnoreCase("null"))
		{
			constantValue = null;
		}
		else if (literals.equalsIgnoreCase("true"))
		{
			constantValue = Boolean.TRUE;
		}
		else if (literals.equalsIgnoreCase("false"))
		{
			constantValue = Boolean.FALSE;
		}
		else if (isNumber(literals))
		{
			if (literals.indexOf('.') > -1)
			{
				constantValue = Double.valueOf(literals);
			}
			else
			{
				constantValue = Long.valueOf(literals);
			}
		}
		else
		{
			constantValue = literals;
		}
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		return constantValue;
	}
	
	@Override
	public CalculateType type()
	{
		return Expression.CONSTANT;
	}
	
	private boolean isNumber(String literals)
	{
		char c = literals.charAt(0);
		return CharType.isDigital(c) || ('-' == c && CharType.isDigital(literals.charAt(1)));
	}
}
