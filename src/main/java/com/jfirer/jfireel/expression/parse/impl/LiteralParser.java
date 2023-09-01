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
        if (c == '\'')
        {
            index += 1;
            while (el.charAt(index) != '\'' && index < el.length())
            {
                index += 1;
            }
            if (index == el.length())
            {
                throw new IllegalStateException("字符串中存在错误的字面量，没有使用'将字面量前后包围。错误范围在" + el.substring(0, parseContext.getIndex()));
            }
            String literal = el.substring(parseContext.getIndex() + 1, index);
            parseContext.setIndex(index + 1);
            parseContext.getOperandStack().push(new LiteralOparand(literal));
            return true;
        }
        else if(c=='"'){
            index += 1;
            while (el.charAt(index) != '"' && index < el.length())
            {
                index += 1;
            }
            if (index == el.length())
            {
                throw new IllegalStateException("字符串中存在错误的字面量，没有使用\"将字面量前后包围。错误范围在" + el.substring(0, parseContext.getIndex()));
            }
            String literal = el.substring(parseContext.getIndex() + 1, index);
            parseContext.setIndex(index + 1);
            parseContext.getOperandStack().push(new LiteralOparand(literal));
            return true;
        }
        else
        {
            return false;
        }
    }
}
