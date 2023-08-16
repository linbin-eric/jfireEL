package com.jfirer.jfireel.expression2;

public class Expression2
{
    public static Operand parse(String el)
    {
        return new ParseContext(el).parse();
    }
}
