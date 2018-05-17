package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class BracketTest
{
	@Test
	public void test()
	{
		int[] array = new int[] { 1, 2, 3, 4 };
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("array", array);
		Lexer lexer = Lexer.parse("array[2]");
		assertEquals(3, lexer.calculate(vars));
	}
	
	@Test
	public void test2()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "12");
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("map", map);
		vars.put("age", "1");
		Lexer lexer = Lexer.parse("map['1']");
		assertEquals("12", lexer.calculate(vars));
		Lexer lexer2 = Lexer.parse("map[age]");
		assertEquals("12", lexer2.calculate(vars));
	}
	
	@Test
	public void test3()
	{
		List<String> list = new ArrayList<String>();
		list.add("1212");
		list.add("13");
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("list", list);
		Lexer lexer = Lexer.parse("list[1]");
		assertEquals("13", lexer.calculate(vars));
	}
	
	@Test
	public void test4()
	{
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("list", list);
		assertEquals("2", Lexer.parse("list[3-2]").calculate(vars));
	}
}
