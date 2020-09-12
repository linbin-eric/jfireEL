package com.jfirer.jfireel;

import com.jfirer.jfireel.expression.Expression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumTest
{
    @Test
    public void test()
    {
        Expression lexer = Expression.parse("T(com.jfirer.jfireel.EnumTest$Name).dd");
        assertEquals(Name.dd, lexer.calculate());
    }

    enum Name
    {
        dd
    }
}
