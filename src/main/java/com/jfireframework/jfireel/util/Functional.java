package com.jfireframework.jfireel.util;

public final class Functional
{
	private int function = 0;
	
	public static Functional build()
	{
		return new Functional();
	}
	
	public Functional setMethodInvokeByCompile(boolean flag)
	{
		if (flag)
		{
			function |= Functions.METHOD_INVOKE_BY_COMPILE;
		}
		else
		{
			function &= ~Functions.METHOD_INVOKE_BY_COMPILE;
		}
		return this;
	}
	
	public Functional setPropertyFetchByUnsafe(boolean flag)
	{
		if (flag)
		{
			function |= Functions.PROPERTY_FETCH_BY_UNSAFE;
		}
		else
		{
			function &= ~Functions.PROPERTY_FETCH_BY_UNSAFE;
		}
		return this;
		
	}
	
	public Functional setRecognizeEveryTime(boolean flag)
	{
		if (flag)
		{
			function |= Functions.RECOGNIZE_EVERY_TIME;
		}
		else
		{
			function &= ~Functions.RECOGNIZE_EVERY_TIME;
		}
		return this;
	}
	
	public int toFunction()
	{
		return function;
	}
}
