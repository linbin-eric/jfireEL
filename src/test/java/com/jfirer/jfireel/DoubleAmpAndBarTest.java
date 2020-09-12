package com.jfirer.jfireel;

import com.jfirer.jfireel.expression.Expression;
import org.junit.Assert;
import org.junit.Test;

public class DoubleAmpAndBarTest
{
    @Test
    public void test()
    {
        Expression parse = Expression.parse("true && 1-2");
        Assert.assertFalse(parse.<Boolean>calculate());
    }

    @Test
    public void test2()
    {
        Expression expression = Expression.parse("false || 2-1");
        Assert.assertTrue(expression.calculate());
    }
}
