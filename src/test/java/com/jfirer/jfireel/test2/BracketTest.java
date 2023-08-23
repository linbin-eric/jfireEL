package com.jfirer.jfireel.test2;

import com.jfirer.jfireel.TestSupport;
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
        parseContext.getStaticClassName().put("EnumTest$Name", Name.class);
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
        String value = person.age + 3 * 2 + "abc";
        vars.put("value", value);
        assertTrue((Boolean) Expression2.parse("home.getPerson().getAge()+3*2+'abc' == value").calculate(vars));
    }

    @Test
    public void test21()
    {
        String value = person.age + 3 * 2 + "abced";
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
        String  result  = (String) operand.calculate();
        assertEquals("ab", result);
    }

    @Test
    public void test30()
    {
        assertEquals(1, ((Number) Expression2.parse("3-1-1").calculate(null)).intValue());
    }

    @Test
    public void test31()
    {
        assertEquals(2, ((Number) Expression2.parse("5-(4-1)").calculate(null)).intValue());
    }

    @Test
    public void test32()
    {
        assertEquals(1, ((Number) Expression2.parse("1*2-1").calculate(null)).intValue());
    }

    /**
     * 测试加法
     */
    @Test
    public void test33()
    {
        Operand lexer = Expression2.parse("1+2");
        assertEquals(3, ((Number) lexer.calculate(new HashMap<String, Object>())).intValue());
        assertEquals(3, ((Number) lexer.calculate(null)).intValue());
    }

    /**
     * 测试加法中出现字符串的类型转化
     */
    @Test
    public void test34()
    {
        assertEquals("11", Expression2.parse("'1'+1").calculate(null));
    }

    /**
     * 测试多个运算符
     */
    @Test
    public void test35()
    {
        assertEquals(12, ((Number) Expression2.parse("1+2+3+6").calculate(null)).intValue());
    }

    /**
     * 测试乘法
     */
    @Test
    public void test36()
    {
        assertEquals(5, ((Number) Expression2.parse("1*5").calculate(null)).intValue());
    }

    /**
     * 测试运算符优先级
     */
    @Test
    public void test37()
    {
        assertEquals(7, ((Number) Expression2.parse("3+2*2").calculate(null)).intValue());
    }

    @Test
    public void test38()
    {
        assertEquals(10, ((Number) Expression2.parse("2*(3+2)").calculate(null)).intValue());
    }

    @Test
    public void test39()
    {
        assertTrue(((Boolean) Expression2.parse("(2+1!=2)==true").calculate()));
    }

    public static int age = 12;

    @Test
    public void test40()
    {
        Operand lexer = Expression2.parse("home.person.age");
        //home 是一个对象，包含一个对象属性 person，age是person中的一个数字属性
        assertEquals(person.age, ((Number) lexer.calculate(vars)).intValue());
        lexer = Expression2.parse("home.person.age");
        assertEquals(person.age, ((Number) lexer.calculate(vars)).intValue());
    }

    // 测试静态属性获取
    @Test
    public void test41()
    {
        ParseContext parseContext = new ParseContext("BracketTest.age");
        parseContext.getStaticClassName().put(BracketTest.class.getSimpleName(), BracketTest.class);
        Operand operand = parseContext.parse();
        int     result  = ((Number) operand.calculate()).intValue();
        assertEquals(age, result);
    }

    @Test
    public void test42()
    {
        assertEquals(1, ((Number) Expression2.parse("2>1?1:2").calculate()).intValue());
        assertEquals(1, ((Number) Expression2.parse("3-2+1>1?1:2").calculate()).intValue());
        assertEquals(1, ((Number) Expression2.parse("3-2+1>1?2-1:2").calculate()).intValue());
        assertEquals(2, ((Number) Expression2.parse("3-2+1<1?2:1+1").calculate()).intValue());
        Map<String, Object> vars  = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("b", false);
        vars.put("map", param);
        assertEquals(2, ((Number) Expression2.parse("map['b']?3:1*2").calculate(vars)).intValue());
        assertEquals(4, ((Number) Expression2.parse("(map['b']?3:2)*2").calculate(vars)).intValue());
    }

    @Test
    public void test43()
    {
        assertEquals(-2, ((Number) Expression2.parse("2>1?1-2>0?-1:-2:3").calculate()).intValue());
    }

    @Test
    public void test44()
    {
        assertEquals(age, ((Number) Expression2.parse("@(com.jfirer.jfireel.test2.BracketTest).age").calculate()).intValue());
    }

    @Test
    public void test45()
    {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "test");
        Expression2.parse("var l=name").calculate(params);
        assertEquals("test", params.get("l"));
        Expression2.parse("var l=name;").calculate(params);
        assertEquals("test", params.get("l"));
    }

    @Test
    public void test46()
    {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "test");
        Operand operand = Expression2.parse("l=name");
        try
        {
            operand.calculate(params);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            assertTrue(true);
            return;
        }
        assertTrue(false);
    }

    @Test
    public void test47()
    {
        Operand operand = Expression2.parseMutli("""
                                                         if(1+age>5)
                                                         {
                                                            var name ='f1';
                                                         }
                                                         else
                                                         { 
                                                            var name = 'f2';
                                                         }
                                                         """);
        Map<String, Object> param = new HashMap<>();
        param.put("age", 3);
        operand.calculate(param);
        assertEquals("f2", param.get("name"));
        param.put("age", 5);
        operand.calculate(param);
        assertEquals("f1", param.get("name"));
    }

    @Test
    public void test48()
    {
        Operand operand = Expression2.parseMutli("""
                                                         var value;
                                                         for(i in array)
                                                         {
                                                    if(i<5)
                                                            {
                                                                value = i;
                                                            }
                                                            else
                                                            {
                                                                break;
                                                            }
                                                         }
                                                         var result;
                                                         if(value==4)
                                                         {
                                                            result = true;
                                                         }
                                                         else
                                                         {
                                                            result=false;
                                                         }
                                                         """);
        Map<String, Object> param = new HashMap<>();
        param.put("array", new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        operand.calculate(param);
        assertTrue((Boolean) param.get("result"));
    }
    @Test
    public void test49()
    {
        Operand operand = Expression2.parseMutli("""
                                                         var value;
                                                         for(i in array)
                                                         {
                                                            if(i<5)
                                                            {
                                                                value = i;
                                                                return;
                                                            }
                                                            else
                                                            {
                                                                break;
                                                            }
                                                         }
                                                         var result;
                                                         if(value==4)
                                                         {
                                                            result = true;
                                                         }
                                                         else
                                                         {
                                                            result=false;
                                                         }
                                                         """);
        Map<String, Object> param = new HashMap<>();
        param.put("array", new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        operand.calculate(param);
        assertFalse(param.containsKey("result"));
    }
}
