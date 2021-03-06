package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.jfireframework.jfireel.expression.Expression;

public class QuestionTest
{
    @Test
    public void test()
    {
        assertEquals(1, Expression.parse("2>1?1:2").calculate());
        assertEquals(1, Expression.parse("3-2+1>1?1:2").calculate());
        assertEquals(1, Expression.parse("3-2+1>1?2-1:2").calculate());
        assertEquals(2, Expression.parse("3-2+1<1?2:1+1").calculate());
        Map<String, Object> vars = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("b", false);
        vars.put("map", param);
        assertEquals(2, Expression.parse("map['b']?3:1*2").calculate(vars));
        assertEquals(4, Expression.parse("(map['b']?3:2)*2").calculate(vars));
    }
}
