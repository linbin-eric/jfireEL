package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PropertyTest extends TestSupport
{
    public static int age = 12;
    
    @Test
    public void test()
    {
        Lexer lexer = Lexer.parse("home.person.age");
        assertEquals(person.age, lexer.calculate(vars));
    }
    
    // 测试静态属性获取
    @Test
    public void test2()
    {
        int result = Lexer.parse("T(com.jfireframework.jfireel.PropertyTest).age").calculate();
        assertEquals(age, result);
    }
}
