package com.jfirer.jfireel;

import com.jfirer.jfireel.expression.Expression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinusTest
{
    @Test
    public void test()
    {
        Expression parse = Expression.parse("3-1-1");
        assertEquals(1, ((Number) parse.calculate(null)).intValue());
    }

    @Test
    public void test2()
    {
        Expression lexer = Expression.parse("5-(4-1)");
        assertEquals(2, ((Number) lexer.calculate(null)).intValue());
    }

    @Test
    public void test3()
    {
        Expression lexer = Expression.parse("1*2-1");
        assertEquals(1, ((Number) lexer.calculate(null)).intValue());
    }
}
