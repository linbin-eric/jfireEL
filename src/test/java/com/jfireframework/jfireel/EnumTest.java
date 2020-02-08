package com.jfireframework.jfireel;

import com.jfireframework.jfireel.expression.Expression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumTest
{
    enum Name
    {
        dd;
    }

    @Test
    public void test()
    {
        Expression lexer = Expression.parse("T(com.jfireframework.jfireel.EnumTest$Name).dd");
        assertEquals(Name.dd, lexer.calculate());
    }
}
