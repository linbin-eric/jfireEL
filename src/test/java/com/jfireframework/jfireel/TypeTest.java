package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TypeTest
{
	@Test
	public void test()
	{
		Class<?> result = Lexer.parse("T(com.jfireframework.jfireel.TypeTest)").calculate();
		assertEquals(TypeTest.class, result);
	}
}
