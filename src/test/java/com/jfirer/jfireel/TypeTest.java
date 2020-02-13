package com.jfirer.jfireel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.jfirer.jfireel.expression.Expression;

public class TypeTest
{
    @Test
    public void test()
    {
        Class<?> result = Expression.parse("T(com.jfirer.jfireel.TypeTest)").calculate();
        assertEquals(TypeTest.class, result);
    }
}
