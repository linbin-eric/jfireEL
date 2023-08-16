package com.jfirer.jfireel.expression2.parse.impl;

import com.jfirer.jfireel.expression.util.CharType;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.VariableOperand;
import com.jfirer.jfireel.expression2.parse.TokenParser;

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
            return true;
        }
        else
        {
            return false;
        }
    }
}
