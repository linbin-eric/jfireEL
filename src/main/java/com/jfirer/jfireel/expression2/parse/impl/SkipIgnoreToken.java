package com.jfirer.jfireel.expression2.parse.impl;

import com.jfirer.jfireel.expression.util.CharType;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.parse.TokenParser;

public class SkipIgnoreToken implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String el    = parseContext.getEl();
        int    index = parseContext.getIndex();
        char   c;
        while (index < el.length())
        {
            c = el.charAt(index);
            if (CharType.isIgnore(c))
            {
                index++;
            }
            else
            {
                break;
            }
        }
        if (index == parseContext.getIndex())
        {
            return false;
        }
        parseContext.setIndex(index);
        return true;
    }
}
