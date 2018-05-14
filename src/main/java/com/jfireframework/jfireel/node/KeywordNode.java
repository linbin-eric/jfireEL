package com.jfireframework.jfireel.node;

import java.util.Map;
import com.jfireframework.jfireel.CalculateType;
import com.jfireframework.jfireel.DefaultKeyWord;
import com.jfireframework.jfireel.KeyWord;

public class KeywordNode implements CalculateNode
{
	private Object	keywordValue;
	private KeyWord	keyWord;
	
	public KeywordNode(String literals)
	{
		keyWord = DefaultKeyWord.getDefaultKeyWord(literals);
		if (literals.equalsIgnoreCase("true"))
		{
			keywordValue = Boolean.TRUE;
		}
		else if (literals.equalsIgnoreCase("false"))
		{
			keywordValue = Boolean.FALSE;
		}
		else if (literals.equalsIgnoreCase("null"))
		{
			keywordValue = null;
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		return keywordValue;
	}
	
	@Override
	public CalculateType type()
	{
		return keyWord;
	}
	
}
