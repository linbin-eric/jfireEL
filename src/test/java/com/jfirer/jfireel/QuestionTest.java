package com.jfirer.jfireel;

import com.jfirer.jfireel.expression.Expression;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class QuestionTest
{
    @Test
    public void test()
    {
        assertEquals(1, ((Number) Expression.parse("2>1?1:2").calculate()).intValue());
        assertEquals(1, ((Number) Expression.parse("3-2+1>1?1:2").calculate()).intValue());
        assertEquals(1, ((Number) Expression.parse("3-2+1>1?2-1:2").calculate()).intValue());
        assertEquals(2, ((Number) Expression.parse("3-2+1<1?2:1+1").calculate()).intValue());
        Map<String, Object> vars  = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("b", false);
        vars.put("map", param);
        assertEquals(2, ((Number) Expression.parse("map['b']?3:1*2").calculate(vars)).intValue());
        assertEquals(4, ((Number) Expression.parse("(map['b']?3:2)*2").calculate(vars)).intValue());
    }

    @Test
    public void test2()
    {
        Expression el = Expression.parse("2>1?1-2>0?-1:-2:3");
        assertEquals(-2, ((Number) el.calculate()).intValue());
    }
}
