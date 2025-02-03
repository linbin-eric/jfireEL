package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.parse.TokenParser;

public class DocTokenParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String el    = parseContext.getEl();
        int    index = parseContext.getIndex();
        if (el.charAt(index) == '#')
        {
            int i = el.indexOf("\n", index);
            if (i == -1)
            {
                throw new IllegalArgumentException("#符号并未单独占据一行，错误");
            }
            parseContext.setIndex(i + 1);
            return true;
        }
        else
        {
            return false;
        }
    }
}
