package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class SyntaxTest
{
	@Test
	public void test()
	{
		Syntax parse = Syntax.parse("hello ${name},my age is ${age+2}");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "ll");
		params.put("age", 10);
		assertEquals("hello ll,my age is 12", parse.calculate(params));
	}
	@Test
	public void test2()
	{
		Syntax parse = Syntax.parse("hello ${name}");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "ll");
		assertEquals("hello ll", parse.calculate(params));
	}
}
