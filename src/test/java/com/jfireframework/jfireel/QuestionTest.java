package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class QuestionTest
{
    @Test
    public void test()
    {
        assertEquals(1, Lexer.parse("2>1?1:2").calculate());
        assertEquals(1, Lexer.parse("3-2+1>1?1:2").calculate());
        assertEquals(1, Lexer.parse("3-2+1>1?2-1:2").calculate());
        assertEquals(2, Lexer.parse("3-2+1<1?2:1+1").calculate());
    }
}
