package com.jfirer.jfireel.expression2.parse.impl;

import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.BooleanOperand;
import com.jfirer.jfireel.expression2.parse.TokenParser;

public class BooleanParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String el    = parseContext.getEl();
        int    index = parseContext.getIndex();
        if (el.charAt(index) == 't' && el.charAt(index + 1) == 'r' && el.charAt(index + 2) == 'u' && el.charAt(index + 3) == 'e')
        {
            parseContext.getOperandStack().push(new BooleanOperand(true));
            parseContext.setIndex(index + 4);
            return true;
        }
        else if (el.charAt(index) == 'f' && el.charAt(index + 1) == 'a' && el.charAt(index + 2) == 'l' && el.charAt(index + 3) == 's' && el.charAt(index + 4) == 'e')
        {
            parseContext.getOperandStack().push(new BooleanOperand(false));
            parseContext.setIndex(index + 5);
            return true;
        }
        else
        {
            return false;
        }
    }
}
