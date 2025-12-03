package cc.jfire.el.expression.parse.impl;

import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operator.LeftParenOperator;
import cc.jfire.el.expression.parse.TokenParser;

public class LeftParenParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String el    = parseContext.getEl();
        int    index = parseContext.getIndex();
        if (el.charAt(index) == '(')
        {
            LeftParenOperator leftParenOperator = new LeftParenOperator(el.substring(0, index));
            leftParenOperator.push(parseContext);
            parseContext.setIndex(index + 1);
            parseContext.getRecognizeToken().add(leftParenOperator);
            return true;
        }
        else
        {
            return false;
        }
    }
}
