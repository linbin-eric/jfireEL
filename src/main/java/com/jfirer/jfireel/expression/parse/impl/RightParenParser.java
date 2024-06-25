package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.TokenType;
import com.jfirer.jfireel.expression.impl.operator.AbstractRightOperator;
import com.jfirer.jfireel.expression.parse.TokenParser;

public class RightParenParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String el    = parseContext.getEl();
        int    index = parseContext.getIndex();
        if (el.charAt(index) == ')')
        {
            new AbstractRightOperator.RightParenOperator(el.substring(0, index + 1)).push(parseContext);
            parseContext.setIndex(index + 1);
            parseContext.setLastToken(TokenType.OPERATOR);
            return true;
        }
        else
        {
            return false;
        }
    }
}
