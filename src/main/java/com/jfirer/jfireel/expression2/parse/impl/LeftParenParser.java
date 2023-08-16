package com.jfirer.jfireel.expression2.parse.impl;

import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operator.LeftParenOperator;
import com.jfirer.jfireel.expression2.parse.TokenParser;

public class LeftParenParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String el    = parseContext.getEl();
        int    index = parseContext.getIndex();
        if (el.charAt(index) == '(')
        {
            new LeftParenOperator(el.substring(0, index)).push(parseContext);
            parseContext.setIndex(index + 1);
            return true;
        }
        else
        {
            return false;
        }
    }
}
