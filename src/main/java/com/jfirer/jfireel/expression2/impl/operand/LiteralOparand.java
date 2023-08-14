package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;

public class LiteralOparand implements Operand
{
    private final String literal;

    public LiteralOparand(String literal)
    {
        this.literal = literal;
    }
}
