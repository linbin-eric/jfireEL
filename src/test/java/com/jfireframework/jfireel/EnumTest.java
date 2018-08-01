package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.jfireframework.jfireel.expression.Expression;

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
