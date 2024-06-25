package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.CharType;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.TokenType;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;
import com.jfirer.jfireel.expression.parse.TokenParser;

public class VariableParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        int    index = parseContext.getIndex();
        String el    = parseContext.getEl();
        if (CharType.isAlphabet(el.charAt(index)))
        {
            index += 1;
            while (index < el.length() && (CharType.isAlphabet(el.charAt(index)) || CharType.isDigital(el.charAt(index))))
            {
                index += 1;
            }
            parseContext.getOperandStack().push(new VariableOperand(el.substring(parseContext.getIndex(), index)));
            parseContext.setIndex(index);
            parseContext.setLastToken(TokenType.OPERAND);
            return true;
        }
        else
        {
            return false;
        }
    }
}
