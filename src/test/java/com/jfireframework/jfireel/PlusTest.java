package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import org.junit.Test;

public class PlusTest
{
	@Test
	public void test()
	{
		Lexer lexer = new Lexer("1+2");
		assertEquals(Long.valueOf(3), lexer.calculate(new HashMap<String, Object>()));
		assertEquals(Long.valueOf(3), lexer.calculate(null));
	}
}
