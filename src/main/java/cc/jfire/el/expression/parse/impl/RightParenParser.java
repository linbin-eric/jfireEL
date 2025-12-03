package cc.jfire.el.expression.parse.impl;

import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operator.RightParenOperator;
import cc.jfire.el.expression.parse.TokenParser;

public class RightParenParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String el    = parseContext.getEl();
        int    index = parseContext.getIndex();
        if (el.charAt(index) == ')')
        {
            RightParenOperator rightParenOperator = new RightParenOperator(el.substring(0, index + 1));
            rightParenOperator.push(parseContext);
            parseContext.setIndex(index + 1);
            parseContext.getRecognizeToken().add(rightParenOperator);
            return true;
        }
        else
        {
            return false;
        }
    }
}
