package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.LiteralOparand;
import com.jfirer.jfireel.expression.parse.TokenParser;

public class LiteralParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        int    index = parseContext.getIndex();
        String el    = parseContext.getEl();
        char   c     = el.charAt(index);
        if (c == '\'' || c == '"' || c == '`')
        {
            extractLiteral(parseContext, c);
            return true;
        }
        else
        {
            return false;
        }
    }

    private static void extractLiteral(ParseContext parseContext, char flag)
    {
        String        el          = parseContext.getEl();
        int           index       = parseContext.getIndex() + 1;
        char          c;
        boolean       literalOver = false;
        StringBuilder builder     = new StringBuilder();
        while (index < el.length())
        {
            c = el.charAt(index);
            if (c == '/')
            {
                if (index + 1 < el.length())
                {
                    char c1 = el.charAt(index + 1);
                    if (c1 == '/')
                    {
                        builder.append('/');
                        index += 2;
                        continue;
                    }
                    else if (c1 == flag)
                    {
                        builder.append(flag);
                        index += 2;
                        continue;
                    }
                    else
                    {
                        throw new IllegalStateException("表达式存在问题，对于转义字符/使用不正确，转义字符后面应该是/或" + flag + "，异常位置在：" + el.substring(0, index));
                    }
                }
            }
            else if (c == flag)
            {
                literalOver = true;
                break;
            }
            else
            {
                builder.append(c);
            }
            index += 1;
        }
        if (!literalOver)
        {
            throw new IllegalStateException("字符串中存在错误的字面量，没有使用" + flag + "将字面量前后包围。错误范围在" + el.substring(0, parseContext.getIndex()));
        }
        parseContext.setIndex(index + 1);
        parseContext.getOperandStack().push(new LiteralOparand(builder.toString()));
    }
}
