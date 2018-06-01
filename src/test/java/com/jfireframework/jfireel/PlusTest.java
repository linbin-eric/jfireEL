package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import org.junit.Test;
import com.jfireframework.jfireel.lexer.Expression;

public class PlusTest
{
    /**
     * 测试加法
     */
    @Test
    public void test()
    {
        Expression lexer = Expression.parse("1+2");
        assertEquals(3, lexer.calculate(new HashMap<String, Object>()));
        assertEquals(3, lexer.calculate(null));
    }
    
    /**
     * 测试加法中出现字符串的类型转化
     */
    @Test
    public void test2()
    {
        Expression lexer = Expression.parse("'1'+1");
        assertEquals("11", lexer.calculate(null));
    }
    
    /**
     * 测试多个运算符
     */
    @Test
    public void test3()
    {
        Expression lexer = Expression.parse("1+2+3+6");
        assertEquals(12, lexer.calculate(null));
    }
    
    /**
     * 测试乘法
     */
    @Test
    public void test4()
    {
        Expression lexer = Expression.parse("1*5");
        assertEquals(5, lexer.calculate(null));
    }
    
    /**
     * 测试运算符优先级
     */
    @Test
    public void test5()
    {
        Expression lexer = Expression.parse("3+2*2");
        assertEquals(7, lexer.calculate(null));
    }
    
    @Test
    public void test6()
    {
        Expression lexer = Expression.parse("2*(3+2)");
        assertEquals(10, lexer.calculate(null));
    }
    
    @Test
    public void test7()
    {
        Expression parse = Expression.parse("(2+1!=2)==true");
        assertTrue((Boolean) parse.calculate(null));
    }
}
