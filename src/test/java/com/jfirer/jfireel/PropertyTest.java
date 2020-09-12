package com.jfirer.jfireel;

import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.util.Functional;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PropertyTest extends TestSupport
{
    public static int age = 12;

    @Test
    public void test()
    {
        Expression lexer = Expression.parse("home.person.age");
        //home 是一个对象，包含一个对象属性 person，age是person中的一个数字属性
        assertEquals(person.age, ((Number) lexer.calculate(vars)).intValue());
        lexer = Expression.parse("home.person.age", Functional.build().setRecognizeEveryTime(false).toFunction());
        assertEquals(person.age, ((Number) lexer.calculate(vars)).intValue());
    }

    // 测试静态属性获取
    @Test
    public void test2()
    {
        int result = Expression.parse("T(com.jfirer.jfireel.PropertyTest).age").calculate();
        assertEquals(age, result);
    }
}
