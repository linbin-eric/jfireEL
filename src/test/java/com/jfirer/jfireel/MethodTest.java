package com.jfirer.jfireel;

import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.util.Functional;
import org.junit.Test;

import static org.junit.Assert.*;

public class MethodTest extends TestSupport
{
    @Test
    public void test()
    {
        Expression lexer = Expression.parse("home.person.getAge()");
        assertEquals(person.age, ((Number) lexer.calculate(vars)).intValue());
    }

    @Test
    public void test2()
    {
        Expression lexer = Expression.parse("home.getPerson().getAge()");
        assertEquals(person.age, ((Number) lexer.calculate(vars)).intValue());
    }

    @Test
    public void test3()
    {
        Expression lexer = Expression.parse("home.getPerson().getAge()+3");
        assertEquals(person.age + 3, ((Number) lexer.calculate(vars)).intValue());
    }

    @Test
    public void test4()
    {
        Expression lexer = Expression.parse("home.getPerson().getAge()+3+'abc'");
        assertEquals(person.age + 3 + "abc", lexer.calculate(vars));
    }

    @Test
    public void test5()
    {
        Expression lexer = Expression.parse("home.getPerson().getAge()+3*2+'abc'");
        assertEquals(person.age + 3 * 2 + "abc", lexer.calculate(vars));
    }

    @Test
    public void test6()
    {
        String     value = person.age + 3 * 2 + "abc";
        Expression lexer = Expression.parse("home.getPerson().getAge()+3*2+'abc' == value");
        vars.put("value", value);
        assertTrue(lexer.calculate(vars));
    }

    @Test
    public void test7()
    {
        String     value = person.age + 3 * 2 + "abced";
        Expression lexer = Expression.parse("home.getPerson().getAge()+3*2+'abc' != value");
        vars.put("value", value);
        assertTrue(lexer.calculate(vars));
    }

    @Test
    public void test8()
    {
        Expression lexer = Expression.parse("home.personAge(person)");
        assertEquals(person.getAge(), ((Number) lexer.calculate(vars)).intValue());
    }

    @Test
    public void test9()
    {
        Expression lexer = Expression.parse("home.personAge2(person.getAge())");
        assertEquals(person.getAge(), ((Number) lexer.calculate(vars)).intValue());
    }

    @Test
    public void test10()
    {
        Expression lexer = Expression.parse("home.personAge2(person.getAge()+2)");
        assertEquals(person.getAge() + 2, ((Number) lexer.calculate(vars)).intValue());
    }

    @Test
    public void test11()
    {
        Expression lexer = Expression.parse("home.name(person.getAge() + '12')");
        assertEquals(person.age + "12", lexer.calculate(vars));
    }

    @Test
    public void test12()
    {
        String value = person.age + "12";
        vars.put("value", value);
        Expression lexer = Expression.parse("home.bool(person.getAge() + '12' != value)");
        assertFalse(lexer.calculate(vars));
    }

    @Test
    public void test13()
    {
        String value = person.age + "12";
        vars.put("value", value);
        Expression lexer = Expression.parse("home.bool(person.getAge() + '12' != value)", Functional.build().setMethodInvokeByCompile(true).toFunction());
        assertFalse(lexer.calculate(vars));
    }

    @Test
    public void test14()
    {
        int result = Expression.parse("home.plus(3,4)").calculate(vars);
        assertEquals(7, result);
    }

    // 测试静态方法
    @Test
    public void test15()
    {
        String result = Expression.parse("T(java.lang.String).valueOf('ab')").calculate();
        assertEquals("ab", result);
    }
}
