package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.TokenType;
import com.jfirer.jfireel.expression.impl.operand.BooleanOperand;
import com.jfirer.jfireel.expression.parse.TokenParser;

public class BooleanParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String el    = parseContext.getEl();
        int    index = parseContext.getIndex();
        if (index + 3 < el.length() && el.charAt(index) == 't' && el.charAt(index + 1) == 'r' && el.charAt(index + 2) == 'u' && el.charAt(index + 3) == 'e')
        {
            parseContext.getOperandStack().push(new BooleanOperand(true));
            parseContext.setIndex(index + 4);
            parseContext.setLastToken(TokenType.OPERAND);
            return true;
        }
        else if (index + 4 < el.length() && el.charAt(index) == 'f' && el.charAt(index + 1) == 'a' && el.charAt(index + 2) == 'l' && el.charAt(index + 3) == 's' && el.charAt(index + 4) == 'e')
        {
            parseContext.getOperandStack().push(new BooleanOperand(false));
            parseContext.setIndex(index + 5);
            parseContext.setLastToken(TokenType.OPERAND);
            return true;
        }
        else
        {
            return false;
        }
    }
}
