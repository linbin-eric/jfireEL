package cc.jfire.el.expression.parse;

import cc.jfire.el.expression.ParseContext;

public interface TokenParser
{
    boolean parse(ParseContext parseContext);
}
