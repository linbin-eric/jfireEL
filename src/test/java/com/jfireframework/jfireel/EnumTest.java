package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class EnumTest
{
	enum Name
	{
		dd;
	}
	
	@Test
	public void test()
	{
		Lexer lexer = Lexer.parse("T(com.jfireframework.jfireel.EnumTest$Name).dd");
		assertEquals(Name.dd, lexer.calculate());
	}
}
