package cc.jfire.el.expression.parse.impl;

import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operand.NullOperand;
import cc.jfire.el.expression.parse.TokenParser;

public class NullParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String el    = parseContext.getEl();
        int    index = parseContext.getIndex();
        if (el.charAt(index) == 'n' && index + 4 < el.length() && el.substring(index, index + 4).equalsIgnoreCase("null"))
        {
            NullOperand nullOperand = new NullOperand();
            parseContext.getOperandStack().push(nullOperand);
            parseContext.setIndex(index + 4);
            parseContext.getRecognizeToken().add(nullOperand);
            return true;
        }
        else
        {
            return false;
        }
    }
}
