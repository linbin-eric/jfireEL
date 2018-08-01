package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.jfireframework.jfireel.expression.Expression;
import com.jfireframework.jfireel.expression.util.Functional;

public class PropertyTest extends TestSupport
{
    public static int age = 12;
    
    @Test
    public void test()
    {
        Expression lexer = Expression.parse("home.person.age");
        assertEquals(person.age, lexer.calculate(vars));
        lexer = Expression.parse("home.person.age", Functional.build().setPropertyFetchByUnsafe(true).setRecognizeEveryTime(false).toFunction());
        assertEquals(person.age, lexer.calculate(vars));
    }
    
    // 测试静态属性获取
    @Test
    public void test2()
    {
        int result = Expression.parse("T(com.jfireframework.jfireel.PropertyTest).age").calculate();
        assertEquals(age, result);
    }
}
