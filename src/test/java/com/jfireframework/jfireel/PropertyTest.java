package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.jfireframework.jfireel.lexer.Lexer;
import com.jfireframework.jfireel.lexer.util.Functional;

public class PropertyTest extends TestSupport
{
	public static int age = 12;
	
	@Test
	public void test()
	{
		Lexer lexer = Lexer.parse("home.person.age");
		assertEquals(person.age, lexer.calculate(vars));
		lexer = Lexer.parse("home.person.age", Functional.build().setPropertyFetchByUnsafe(true).setRecognizeEveryTime(false).toFunction());
		assertEquals(person.age, lexer.calculate(vars));
	}
	
	// 测试静态属性获取
	@Test
	public void test2()
	{
		int result = Lexer.parse("T(com.jfireframework.jfireel.PropertyTest).age").calculate();
		assertEquals(age, result);
	}
}
