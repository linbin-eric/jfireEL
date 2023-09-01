package com.jfirer.jfireel;

import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.template.Template;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class BracketTest extends TestSupport
{
    @Test
    public void test1()
    {
        int[]               array = new int[]{1, 2, 3, 4};
        Map<String, Object> vars  = new HashMap<String, Object>();
        vars.put("array", array);
        Operand lexer = Expression.parse("array[2]");
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
        Operand lexer = Expression.parse("map['1']");
        assertEquals("12", lexer.calculate(vars));
        Operand lexer2 = Expression.parse("map[age]");
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
        Operand lexer = Expression.parse("list[1]");
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
        assertEquals("2", Expression.parse("list[3-2]").calculate(vars));
    }

    @Test
    public void test5()
    {
        boolean result = (boolean) Expression.parse("2>1").calculate(null);
        assertTrue(result);
        result = (boolean) Expression.parse("2>1.0").calculate(null);
        assertTrue(result);
    }

    @Test
    public void test6()
    {
        boolean result = (boolean) Expression.parse("1<2").calculate(null);
        assertTrue(result);
        result = (boolean) Expression.parse("1.0<2").calculate(null);
        assertTrue(result);
    }

    @Test
    public void test7()
    {
        assertFalse((Boolean) Expression.parse("1>2*2-3").calculate(null));
    }

    @Test
    public void test8()
    {
        assertEquals(2, ((Number) Expression.parse("4/2").calculate(null)).intValue());
    }

    @Test
    public void test9()
    {
        float f = 5 / 2f;
        assertEquals(f, ((Number) Expression.parse("5.0/2").calculate(null)).floatValue(), 0.00000000001);
    }

    @Test
    public void test10()
    {
        assertEquals(3, ((Number) Expression.parse("1+4/2").calculate()).intValue());
    }

    @Test
    public void test11()
    {
        Assert.assertFalse((Boolean) Expression.parse("true && 1-2>0").calculate());
    }

    @Test
    public void test12()
    {
        Assert.assertTrue((Boolean) Expression.parse("false || 2-1>0").calculate());
    }

    @Test
    public void test13()
    {
        Map<String, Object> m = new HashMap<>();
        m.put("BLOODPRESSURE1", 100);
        Assert.assertTrue((Boolean) Expression.parse("BLOODPRESSURE1<140&&BLOODPRESSURE1>90").calculate(m));
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
        assertEquals(person.age, ((Number) Expression.parse("home.person.getAge()").calculate(vars)).intValue());
    }

    @Test
    public void test16()
    {
        assertEquals(person.age, ((Number) Expression.parse("home.getPerson().getAge()").calculate(vars)).intValue());
    }

    @Test
    public void test17()
    {
        assertEquals(person.age + 3, ((Number) Expression.parse("home.getPerson().getAge()+3").calculate(vars)).intValue());
    }

    @Test
    public void test18()
    {
        assertEquals(person.age + 3 + "abc", Expression.parse("home.getPerson().getAge()+3+'abc'").calculate(vars));
    }

    @Test
    public void test19()
    {
        assertEquals(person.age + 3 * 2 + "abc", Expression.parse("home.getPerson().getAge()+3*2+'abc'").calculate(vars));
    }

    @Test
    public void test20()
    {
        String value = person.age + 3 * 2 + "abc";
        vars.put("value", value);
        assertTrue((Boolean) Expression.parse("home.getPerson().getAge()+3*2+'abc' == value").calculate(vars));
    }

    @Test
    public void test21()
    {
        String value = person.age + 3 * 2 + "abced";
        vars.put("value", value);
        assertTrue((Boolean) Expression.parse("home.getPerson().getAge()+3*2+'abc' != value").calculate(vars));
    }

    @Test
    public void test22()
    {
        assertEquals(person.getAge(), ((Number) Expression.parse("home.personAge(person)").calculate(vars)).intValue());
    }

    @Test
    public void test23()
    {
        assertEquals(person.getAge(), ((Number) Expression.parse("home.personAge2(person.getAge())").calculate(vars)).intValue());
    }

    @Test
    public void test24()
    {
        assertEquals(person.getAge() + 2, ((Number) Expression.parse("home.personAge2(person.getAge()+2)").calculate(vars)).intValue());
    }

    @Test
    public void test25()
    {
        assertEquals(person.age + "12", Expression.parse("home.name(person.getAge() + '12')").calculate(vars));
    }

    @Test
    public void test26()
    {
        String value = person.age + "12";
        vars.put("value", value);
        assertFalse((Boolean) Expression.parse("home.bool(person.getAge() + '12' != value)").calculate(vars));
    }

    @Test
    public void test27()
    {
        String value = person.age + "12";
        vars.put("value", value);
        assertFalse((Boolean) Expression.parse("home.bool(person.getAge() + '12' != value)").calculate(vars));
    }

    @Test
    public void test28()
    {
        int result = ((Number) Expression.parse("home.plus(3,4)").calculate(vars)).intValue();
        assertEquals(7, result);
    }

    // 测试静态方法
    @Test
    public void test29()
    {
        ParseContext parseContext = new ParseContext("String.valueOf('ab')");
        parseContext.registerClass("String", String.class);
        Operand operand = parseContext.parse();
        String  result  = (String) operand.calculate();
        assertEquals("ab", result);
    }

    @Test
    public void test30()
    {
        assertEquals(1, ((Number) Expression.parse("3-1-1").calculate(null)).intValue());
    }

    @Test
    public void test31()
    {
        assertEquals(2, ((Number) Expression.parse("5-(4-1)").calculate(null)).intValue());
    }

    @Test
    public void test32()
    {
        assertEquals(1, ((Number) Expression.parse("1*2-1").calculate(null)).intValue());
    }

    /**
     * 测试加法
     */
    @Test
    public void test33()
    {
        Operand lexer = Expression.parse("1+2");
        assertEquals(3, ((Number) lexer.calculate(new HashMap<String, Object>())).intValue());
        assertEquals(3, ((Number) lexer.calculate(null)).intValue());
    }

    /**
     * 测试加法中出现字符串的类型转化
     */
    @Test
    public void test34()
    {
        assertEquals("11", Expression.parse("'1'+1").calculate(null));
    }

    /**
     * 测试多个运算符
     */
    @Test
    public void test35()
    {
        assertEquals(12, ((Number) Expression.parse("1+2+3+6").calculate(null)).intValue());
    }

    /**
     * 测试乘法
     */
    @Test
    public void test36()
    {
        assertEquals(5, ((Number) Expression.parse("1*5").calculate(null)).intValue());
    }

    /**
     * 测试运算符优先级
     */
    @Test
    public void test37()
    {
        assertEquals(7, ((Number) Expression.parse("3+2*2").calculate(null)).intValue());
    }

    @Test
    public void test38()
    {
        assertEquals(10, ((Number) Expression.parse("2*(3+2)").calculate(null)).intValue());
    }

    @Test
    public void test39()
    {
        assertTrue(((Boolean) Expression.parse("(2+1!=2)==true").calculate()));
    }

    public static int age = 12;

    @Test
    public void test40()
    {
        Operand lexer = Expression.parse("home.person.age");
        //home 是一个对象，包含一个对象属性 person，age是person中的一个数字属性
        assertEquals(person.age, ((Number) lexer.calculate(vars)).intValue());
        lexer = Expression.parse("home.person.age");
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
        assertEquals(1, ((Number) Expression.parse("2>1?1:2").calculate()).intValue());
        assertEquals(1, ((Number) Expression.parse("3-2+1>1?1:2").calculate()).intValue());
        assertEquals(1, ((Number) Expression.parse("3-2+1>1?2-1:2").calculate()).intValue());
        assertEquals(2, ((Number) Expression.parse("3-2+1<1?2:1+1").calculate()).intValue());
        Map<String, Object> vars  = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("b", false);
        vars.put("map", param);
        assertEquals(2, ((Number) Expression.parse("map['b']?3:1*2").calculate(vars)).intValue());
        assertEquals(4, ((Number) Expression.parse("(map['b']?3:2)*2").calculate(vars)).intValue());
    }

    @Test
    public void test43()
    {
        assertEquals(-2, ((Number) Expression.parse("2>1?1-2>0?-1:-2:3").calculate()).intValue());
    }

    @Test
    public void test44()
    {
        assertEquals(age, ((Number) Expression.parse("@(com.jfirer.jfireel.BracketTest).age").calculate()).intValue());
    }

    @Test
    public void test45()
    {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "test");
        Expression.parse("var l=name").calculate(params);
        assertEquals("test", params.get("l"));
        Expression.parse("var l=name;").calculate(params);
        assertEquals("test", params.get("l"));
    }

    @Test
    public void test46()
    {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "test");
        Operand operand = Expression.parse("l=name");
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
        Operand operand = Expression.parseMutli("""
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
        Operand operand = Expression.parseMutli("""
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
        Operand operand = Expression.parseMutli("""
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

    @Test
    public void test50()
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        params.put("age", 10);
        assertEquals("hello ll,my age is 12", Template.parse("hello ${name},my age is ${age+2}").render(params));
    }

    @Test
    public void test51()
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        assertEquals("hello ll", Template.parse("hello ${name}").render(params));
    }

    @Test
    public void test52()
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        params.put("age", 10);
        assertEquals("hello, my name is ll  ", Template.parse("hello,<%if(age>2){%> my name is ${name}<%}%>  ").render(params));
    }

    @Test
    public void test53()
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        params.put("age", 10);
        assertEquals("hello, my name is ll", Template.parse("hello,<%if(age>2){%> my name is <%if(name=='ll'){%>${name}<%}%><%}%>").render(params));
    }

    @Test
    public void test54()
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        params.put("age", 1);
        assertEquals("hello, ll", Template.parse("hello,<%if(age>2){%> ${name} <%}%><%else{%> ll<%}%>").render(params));
    }

    @Test
    public void test55()
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        params.put("age", 1);
        assertEquals("hello, ll", Template.parse("hello,<%if(age>2){%> ${name} <% } else {%> ll<%}%>").render(params));
    }

    @Test
    public void test56()
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        params.put("age", 1);
        assertEquals("hello, age<5", Template.parse("hello,<%if(age>10){%> ${name} <%} else if(age>5){%> age >5 <%} else {%> age<5<%}%>").render(params));
    }

    @Test
    public void test57()
    {
        List<String> list = new LinkedList<String>();
        list.add("name1");
        list.add("name2");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("list", list);
        assertEquals("hello, name1 name2", Template.parse("hello,<% for (name in list) {%> ${name}<%}%>").render(params));
    }

    @Test
    public void test58()
    {
        List<String> list = new LinkedList<String>();
        list.add("name1");
        list.add("name2");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("list", list);
        assertEquals("hello, name1", Template.parse("hello,<% for (name in list) {%><% if(name=='name1'){%> ${name}<%}%><%}%>").render(params));
    }

    @Test
    public void test59()
    {
        List<String> list = new LinkedList<String>();
        list.add("name1");
        list.add("name2");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("list", list);
        params.put("age", 11);
        assertEquals(" name1  name2 ", Template.parse("<% if(age>10){ for(name in list){ %> ${name} <%}}%>").render(params));
    }

    @Test
    public void test60()
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        params.put("age", 10);
        assertEquals("hello ll,my age is 12", Template.parse("hello ${name},my age is ${age+2}").render(params));
    }

    @Test
    public void test61()
    {
        assertEquals(BracketTest.class, Expression.parse("@(com.jfirer.jfireel.BracketTest)").calculate());
    }

    @Test
    public void test62()
    {
        String content = """
                if(i<5){
                var name =5;
                return name;
                }""";
        Operand             operand = Expression.parseMutli(content);
        Map<String, Object> map     = new HashMap<>();
        map.put("i", 3);
        assertEquals(5, ((Integer) operand.calculate(map)).intValue());
    }

    @Test
    public void test63()
    {
        String content = """
                var name="yl";""";
        Map<String, Object> map     = new HashMap<>();
        Operand             operand = Expression.parseMutli(content);
        operand.calculate(map);
        assertEquals("yl", ((String) map.get("name")));
    }
}
