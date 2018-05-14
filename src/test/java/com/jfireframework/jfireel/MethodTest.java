package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MethodTest extends TestSupport
{
	@Test
	public void test()
	{
		Lexer lexer = Lexer.parse("home.person.getAge()");
		assertEquals(person.age, lexer.calculate(vars));
	}
	
	@Test
	public void test2()
	{
		Lexer lexer = Lexer.parse("home.getPerson().getAge()");
		assertEquals(person.age, lexer.calculate(vars));
	}
	
	@Test
	public void test3()
	{
		Lexer lexer = Lexer.parse("home.getPerson().getAge()+3");
		assertEquals(Long.valueOf(person.age + 3), lexer.calculate(vars));
	}
	
	@Test
	public void test4()
	{
		Lexer lexer = Lexer.parse("home.getPerson().getAge()+3+'abc'");
		assertEquals(person.age + 3 + "abc", lexer.calculate(vars));
	}
	
	@Test
	public void test5()
	{
		Lexer lexer = Lexer.parse("home.getPerson().getAge()+3*2+'abc'");
		assertEquals(person.age + 3 * 2 + "abc", lexer.calculate(vars));
	}
	
	@Test
	public void test6()
	{
		String value = person.age + 3 * 2 + "abc";
		Lexer lexer = Lexer.parse("home.getPerson().getAge()+3*2+'abc' == value");
		vars.put("value", value);
		assertTrue((Boolean) lexer.calculate(vars));
	}
	
	@Test
	public void test7()
	{
		String value = person.age + 3 * 2 + "abced";
		Lexer lexer = Lexer.parse("home.getPerson().getAge()+3*2+'abc' != value");
		vars.put("value", value);
		assertTrue((Boolean) lexer.calculate(vars));
	}
}
