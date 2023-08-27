package com.jfirer.jfireel.benchmark;

import com.jfirer.jfireel.TestSupport;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression2.Expression2;
import com.jfirer.jfireel.expression2.Operand;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PertMark
{
    public Operand             lexer_new = Expression2.parse("home.bool(true)");
    private Expression expression = Expression.parse("home.bool(true)");
    public Map<String, Object> vars      = new HashMap<String, Object>();
    public TestSupport.Person  person;
    public TestSupport.Home    home;

    @Before
    public void before()
    {
        home        = new TestSupport.Home();
        person      = new TestSupport.Person();
        person.age  = 14;
        home.person = person;
        vars.put("person", person);
        vars.put("home", home);
        String value = person.age + "12";
        vars.put("value", value);
        System.out.println("初始化一次");
    }

    @Test
    public void test()
    {
        for (int i = 0; i < 1000000000; i++)
        {
            lexer_new.calculate(vars);
            expression.calculate(vars);
        }
    }
}
