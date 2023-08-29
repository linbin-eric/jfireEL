package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.NullOperand;
import com.jfirer.jfireel.expression.parse.TokenParser;

public class NullParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String el    = parseContext.getEl();
        int    index = parseContext.getIndex();
        if (el.charAt(index) == 'n' && index + 4 < el.length() && el.substring(index, index + 4).equalsIgnoreCase("null"))
        {
            parseContext.getOperandStack().push(new NullOperand());
            parseContext.setIndex(index + 4);
            return true;
        }
        else
        {
            return false;
        }
    }
}
