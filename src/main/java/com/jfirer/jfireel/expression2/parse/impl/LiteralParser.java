package com.jfirer.jfireel.expression2.parse.impl;

import com.jfirer.jfireel.expression.util.CharType;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.LiteralOparand;
import com.jfirer.jfireel.expression2.parse.TokenParser;

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
            while (CharType.isAlphabet(el.charAt(index)))
            {
                index += 1;
            }
            if (el.charAt(index) == '\'')
            {
                String literal = el.substring(parseContext.getIndex() + 1, index);
                parseContext.setIndex(index + 1);
                parseContext.getOperandStack().push(new LiteralOparand(literal));
                return true;
            }
            else
            {
                throw new IllegalStateException("字符串中存在错误的字面量，没有使用'将字面量前后包围。错误范围在:" + el.substring(0, index));
            }
        }
        else
        {
            return false;
        }
    }
}
