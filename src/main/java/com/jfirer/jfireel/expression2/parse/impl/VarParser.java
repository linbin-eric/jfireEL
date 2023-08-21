package com.jfirer.jfireel.expression2.parse.impl;

import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operator.VarOperator;
import com.jfirer.jfireel.expression2.parse.TokenParser;

public class VarParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        int    index = parseContext.getIndex();
        String el    = parseContext.getEl();
        if (el.charAt(index) == 'v')
        {
            if (index + 2 < el.length() && el.charAt(index + 1) == 'a' && el.charAt(index + 2) == 'r')
            {
                new VarOperator().push(parseContext);
                parseContext.setIndex(index + 3);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
