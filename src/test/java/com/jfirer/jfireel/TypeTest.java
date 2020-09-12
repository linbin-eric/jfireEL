package com.jfirer.jfireel;

import com.jfirer.jfireel.expression.Expression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeTest
{
    @Test
    public void test()
    {
        Class<?> result = Expression.parse("T(com.jfirer.jfireel.TypeTest)").calculate();
        assertEquals(TypeTest.class, result);
    }
}
