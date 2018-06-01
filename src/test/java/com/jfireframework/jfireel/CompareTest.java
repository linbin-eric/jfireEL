package com.jfireframework.jfireel;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.jfireframework.jfireel.lexer.Expression;

public class CompareTest
{
	@Test
	public void test()
	{
		boolean result = Expression.parse("2>1").calculate();
		assertTrue(result);
		result = Expression.parse("2>1.0").calculate();
		assertTrue(result);
	}
	
	@Test
	public void test2()
	{
		boolean result = Expression.parse("1<2").calculate();
		assertTrue(result);
		result = Expression.parse("1.0<2").calculate();
		assertTrue(result);
	}
	
}
