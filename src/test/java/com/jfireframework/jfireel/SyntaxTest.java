package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.jfireframework.jfireel.syntax.Syntax;

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
	
	@Test
	public void test3()
	{
		Syntax syntax = Syntax.parse("hello,<%if(age>2){%> my name is ${name}<%}%>  ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "ll");
		params.put("age", 10);
		assertEquals("hello, my name is ll  ", syntax.calculate(params));
	}
	
	@Test
	public void test4()
	{
		Syntax syntax = Syntax.parse("hello,<%if(age>2){%> my name is <%if(name=='ll'){%>${name}<%}%><%}%>");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "ll");
		params.put("age", 10);
		assertEquals("hello, my name is ll", syntax.calculate(params));
	}
	
	@Test
	public void test5()
	{
		Syntax syntax = Syntax.parse("hello,<%if(age>2){%> ${name} <%}%><%else{%> ll<%}%>");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "ll");
		params.put("age", 1);
		assertEquals("hello, ll", syntax.calculate(params));
	}
	@Test
	public void test6()
	{
		Syntax syntax = Syntax.parse("hello,<%if(age>2){%> ${name} <% } else {%> ll<%}%>");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "ll");
		params.put("age", 1);
		assertEquals("hello, ll", syntax.calculate(params));
	}
}
