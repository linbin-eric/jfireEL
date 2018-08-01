package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.jfireframework.jfireel.expression.Expression;

public class TypeTest
{
    @Test
    public void test()
    {
        Class<?> result = Expression.parse("T(com.jfireframework.jfireel.TypeTest)").calculate();
        assertEquals(TypeTest.class, result);
    }
}
