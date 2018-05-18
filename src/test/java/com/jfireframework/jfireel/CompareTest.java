package com.jfireframework.jfireel;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CompareTest
{
	@Test
	public void test()
	{
		boolean result = Lexer.parse("2>1").calculate();
		assertTrue(result);
		result = Lexer.parse("2>1.0").calculate();
		assertTrue(result);
	}
	
	@Test
	public void test2()
	{
		boolean result = Lexer.parse("1<2").calculate();
		assertTrue(result);
		result = Lexer.parse("1.0<2").calculate();
		assertTrue(result);
	}
	
}
