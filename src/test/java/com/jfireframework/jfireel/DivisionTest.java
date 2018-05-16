package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DivisionTest
{
    @Test
    public void test()
    {
        Lexer lexer = Lexer.parse("4/2");
        assertEquals(2, lexer.calculate(null));
    }
    
    @Test
    public void test1()
    {
        Lexer lexer = Lexer.parse("5.0/2");
        float f = 5 / 2f;
        assertEquals(f, (Float) lexer.calculate(null), 0.00000000001);
    }
    
    @Test
    public void test2()
    {
        Lexer lexer = Lexer.parse("1+4/2");
        assertEquals(3, lexer.calculate(null));
    }
}
