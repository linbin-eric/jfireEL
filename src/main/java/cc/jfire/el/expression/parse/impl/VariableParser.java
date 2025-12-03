package cc.jfire.el.expression.parse.impl;

import cc.jfire.el.expression.CharType;
import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operand.VariableOperand;
import cc.jfire.el.expression.parse.TokenParser;

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
