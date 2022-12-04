package com.jfirer.jfireel;

import com.jfirer.jfireel.expression.Expression;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void test3()
    {
        Expression expression = Expression.parse("BLOODPRESSURE1<140&&BLOODPRESSURE1>90");
        Map<String, Object> m = new HashMap<>();
        m.put("BLOODPRESSURE1", 100);
        Assert.assertTrue(expression.calculate(m));
    }
}
