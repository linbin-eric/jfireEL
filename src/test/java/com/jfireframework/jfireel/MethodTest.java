package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
	
	@Test
	public void test8()
	{
		Lexer lexer = Lexer.parse("home.personAge(person)");
		assertEquals(person.getAge(), lexer.calculate(vars));
	}
	
	@Test
	public void test9()
	{
		Lexer lexer = Lexer.parse("home.personAge2(person.getAge())");
		assertEquals(person.getAge(), lexer.calculate(vars));
	}
	
	@Test
	public void test10()
	{
		Lexer lexer = Lexer.parse("home.personAge2(person.getAge()+2)");
		assertEquals(person.getAge() + 2, lexer.calculate(vars));
	}
	
	@Test
	public void test11()
	{
		Lexer lexer = Lexer.parse("home.name(person.getAge() + '12')");
		assertEquals(person.age + "12", lexer.calculate(vars));
	}
	
	@Test
	public void test12()
	{
		String value = person.age + "12";
		vars.put("value", value);
		Lexer lexer = Lexer.parse("home.bool(person.getAge() + '12' != value)");
		assertFalse((Boolean) lexer.calculate(vars));
	}
	
	@Test
	public void perTest()
	{
		String value = person.age + "12";
		vars.put("value", value);
		Lexer lexer = Lexer.parse("home.bool(person.getAge() + '12' != value)");
		int preheat = 100;
		int count = 10000000;
		for (int i = 0; i < preheat; i++)
		{
			lexer.calculate(vars);
		}
		long t0 = System.nanoTime();
		for (int i = 0; i < count; i++)
		{
			lexer.calculate(vars);
		}
		long t1 = System.nanoTime();
		System.out.println("计算" + (count / 10000) + "万次耗时:" + (t1 - t0) / 1000 / 1000 + "毫秒");
		
	}
}
