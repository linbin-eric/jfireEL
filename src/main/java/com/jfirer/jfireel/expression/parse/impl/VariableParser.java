package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.CharType;
import com.jfirer.jfireel.expression.ParseContext;
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
            VariableOperand variableOperand = new VariableOperand(el.substring(parseContext.getIndex(), index));
            parseContext.getOperandStack().push(variableOperand);
            parseContext.setIndex(index);
            parseContext.getRecognizeToken().add(variableOperand);
            return true;
        }
        else
        {
            return false;
        }
    }
}
