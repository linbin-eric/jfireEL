package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.jfireframework.jfireel.lexer.Expression;

public class MinusTest
{
	@Test
	public void test()
	{
		Expression parse = Expression.parse("3-1-1");
		assertEquals(1, parse.calculate(null));
	}
	
	@Test
	public void test2()
	{
		Expression lexer = Expression.parse("5-(4-1)");
		assertEquals(2, lexer.calculate(null));
	}
	
	@Test
	public void test3()
	{
		Expression lexer = Expression.parse("1*2-1");
		assertEquals(1, lexer.calculate(null));
	}
}
