package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PropertyTest extends TestSupport
{
	
	@Test
	public void test()
	{
		Lexer lexer = Lexer.parse("home.person.age");
		assertEquals(person.age, lexer.calculate(vars));
	}
	
}
