package com.jfirer.jfireel;

import com.jfirer.jfireel.expression.Expression;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CompareTest
{
    @Test
    public void test()
    {
        boolean result = Expression.parse("2>1").calculate();
        assertTrue(result);
        result = Expression.parse("2>1.0").calculate();
        assertTrue(result);
    }

    @Test
    public void test2()
    {
        boolean result = Expression.parse("1<2").calculate();
        assertTrue(result);
        result = Expression.parse("1.0<2").calculate();
        assertTrue(result);
    }

    @Test
    public void test3()
    {
        Expression ex = Expression.parse("1>2*2-3");
        assertFalse(ex.calculate());
    }
}
