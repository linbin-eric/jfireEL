package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;

import java.util.Map;

public class LiteralOparand implements Operand
{
    private final String literal;

    public LiteralOparand(String literal)
    {
        this.literal = literal;
    }

    @Override
    public Object calculate(Map<String, Object> param)
    {
        return literal;
    }
}
