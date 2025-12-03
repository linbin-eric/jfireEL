package cc.jfire.el.expression.parse.impl;

import cc.jfire.el.expression.CharType;
import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.parse.TokenParser;

public class SkipIgnoreToken implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String el    = parseContext.getEl();
        int    index = parseContext.getIndex();
        char   c;
        while (index < el.length())
        {
            c = el.charAt(index);
            if (CharType.isIgnore(c))
            {
                index++;
            }
            else
            {
                break;
            }
        }
        if (index == parseContext.getIndex())
        {
            return false;
        }
        parseContext.setIndex(index);
        return true;
    }
}
