package com.jfirer.jfireel.expression2.parse.impl;

import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operator.RightParenOperator;
import com.jfirer.jfireel.expression2.parse.TokenParser;

public class RightParenParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String el    = parseContext.getEl();
        int    index = parseContext.getIndex();
        if (el.charAt(index) == ')')
        {
            new RightParenOperator(el.substring(0, index+1)).push(parseContext);
            parseContext.setIndex(index + 1);
            return true;
        }
        else
        {
            return false;
        }
    }
}
