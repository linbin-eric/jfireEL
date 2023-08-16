package com.jfirer.jfireel.test2;

import com.jfirer.jfireel.EnumTest;
import com.jfirer.jfireel.TestSupport;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.util.Functional;
import com.jfirer.jfireel.expression2.Expression2;
import com.jfirer.jfireel.expression2.Operand;
import com.jfirer.jfireel.expression2.ParseContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BracketTest extends TestSupport
{
    @Test
    public void test1()
    {
        int[]               array = new int[]{1, 2, 3, 4};
        Map<String, Object> vars  = new HashMap<String, Object>();
        vars.put("array", array);
        Operand lexer = Expression2.parse("array[2]");
        assertEquals(3, ((Integer) lexer.calculate(vars)).intValue());
    }

    @Test
    public void test2()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("1", "12");
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("map", map);
        vars.put("age", "1");
        Operand lexer = Expression2.parse("map['1']");
        assertEquals("12", lexer.calculate(vars));
        Operand lexer2 = Expression2.parse("map[age]");
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
        Operand lexer = Expression2.parse("list[1]");
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
        assertEquals("2", Expression2.parse("list[3-2]").calculate(vars));
    }

    @Test
    public void test5()
    {
        boolean result = (boolean) Expression2.parse("2>1").calculate(null);
        assertTrue(result);
        result = (boolean) Expression2.parse("2>1.0").calculate(null);
        assertTrue(result);
    }

    @Test
    public void test6()
    {
        boolean result = (boolean) Expression2.parse("1<2").calculate(null);
        assertTrue(result);
        result = (boolean) Expression2.parse("1.0<2").calculate(null);
        assertTrue(result);
    }

    @Test
    public void test7()
    {
        assertFalse((Boolean) Expression2.parse("1>2*2-3").calculate(null));
    }

    @Test
    public void test8()
    {
        assertEquals(2, ((Number) Expression2.parse("4/2").calculate(null)).intValue());
    }

    @Test
    public void test9()
    {
        float f = 5 / 2f;
        assertEquals(f, ((Number) Expression2.parse("5.0/2").calculate(null)).floatValue(), 0.00000000001);
    }

    @Test
    public void test10()
    {
        assertEquals(3, ((Number) Expression2.parse("1+4/2").calculate()).intValue());
    }

    @Test
    public void test11()
    {
        Assert.assertFalse((Boolean) Expression2.parse("true && 1-2>0").calculate());
    }

    @Test
    public void test12()
    {
        Assert.assertTrue((Boolean) Expression2.parse("false || 2-1>0").calculate());
    }

    @Test
    public void test13()
    {
        Map<String, Object> m = new HashMap<>();
        m.put("BLOODPRESSURE1", 100);
        Assert.assertTrue((Boolean) Expression2.parse("BLOODPRESSURE1<140&&BLOODPRESSURE1>90").calculate(m));
    }
    enum Name
    {
        dd
    }
    @Test
    public void test14()
    {
        ParseContext parseContext = new ParseContext("EnumTest$Name.dd");
        parseContext.getStaticClassName().put("EnumTest$Name",Name.class);
        assertEquals(BracketTest.Name.dd, parseContext.parse().calculate());
    }
    @Test
    public void test15()
    {
        assertEquals(person.age, ((Number) Expression2.parse("home.person.getAge()").calculate(vars)).intValue());
    }

    @Test
    public void test16()
    {
        assertEquals(person.age, ((Number) Expression2.parse("home.getPerson().getAge()").calculate(vars)).intValue());
    }

    @Test
    public void test17()
    {
        assertEquals(person.age + 3, ((Number) Expression2.parse("home.getPerson().getAge()+3").calculate(vars)).intValue());
    }

    @Test
    public void test18()
    {
        assertEquals(person.age + 3 + "abc", Expression2.parse("home.getPerson().getAge()+3+'abc'").calculate(vars));
    }

    @Test
    public void test19()
    {
        assertEquals(person.age + 3 * 2 + "abc", Expression2.parse("home.getPerson().getAge()+3*2+'abc'").calculate(vars));
    }

    @Test
    public void test20()
    {
        String     value = person.age + 3 * 2 + "abc";
        vars.put("value", value);
        assertTrue((Boolean) Expression2.parse("home.getPerson().getAge()+3*2+'abc' == value").calculate(vars));
    }

    @Test
    public void test21()
    {
        String     value = person.age + 3 * 2 + "abced";
        vars.put("value", value);
        assertTrue((Boolean) Expression2.parse("home.getPerson().getAge()+3*2+'abc' != value").calculate(vars));
    }

    @Test
    public void test22()
    {
        assertEquals(person.getAge(), ((Number) Expression2.parse("home.personAge(person)").calculate(vars)).intValue());
    }

    @Test
    public void test23()
    {
        assertEquals(person.getAge(), ((Number) Expression2.parse("home.personAge2(person.getAge())").calculate(vars)).intValue());
    }

    @Test
    public void test24()
    {
        assertEquals(person.getAge() + 2, ((Number) Expression2.parse("home.personAge2(person.getAge()+2)").calculate(vars)).intValue());
    }

    @Test
    public void test25()
    {
        assertEquals(person.age + "12", Expression2.parse("home.name(person.getAge() + '12')").calculate(vars));
    }

    @Test
    public void test26()
    {
        String value = person.age + "12";
        vars.put("value", value);
        assertFalse((Boolean) Expression2.parse("home.bool(person.getAge() + '12' != value)").calculate(vars));
    }

    @Test
    public void test27()
    {
        String value = person.age + "12";
        vars.put("value", value);
        assertFalse((Boolean) Expression2.parse("home.bool(person.getAge() + '12' != value)").calculate(vars));
    }

    @Test
    public void test28()
    {
        int result = ((Number) Expression2.parse("home.plus(3,4)").calculate(vars)).intValue();
        assertEquals(7, result);
    }

    // 测试静态方法
    @Test
    public void test29()
    {
        ParseContext parseContext = new ParseContext("String.valueOf('ab')");
        parseContext.getStaticClassName().put("String", String.class);
        Operand operand = parseContext.parse();
        String result = (String) operand.calculate();
        assertEquals("ab", result);
    }

}
