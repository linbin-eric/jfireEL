package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operator.LeftParenOperator;
import com.jfirer.jfireel.expression.parse.TokenParser;

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
