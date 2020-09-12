package com.jfirer.jfireel;

import com.jfirer.jfireel.expression.Expression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DivisionTest
{
    @Test
    public void test()
    {
        Expression lexer = Expression.parse("4/2");
        assertEquals(2, ((Number) lexer.calculate(null)).intValue());
    }

    @Test
    public void test1()
    {
        Expression lexer = Expression.parse("5.0/2");
        float      f     = 5 / 2f;
        assertEquals(f, lexer.calculate(null), 0.00000000001);
    }

    @Test
    public void test2()
    {
        Expression lexer = Expression.parse("1+4/2");
        assertEquals(3, ((Integer) lexer.calculate(null)).intValue());
    }
}
