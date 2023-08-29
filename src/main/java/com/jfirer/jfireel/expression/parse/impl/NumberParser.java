package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.CharType;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.NumberOperand;
import com.jfirer.jfireel.expression.parse.TokenParser;

public class NumberParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String el    = parseContext.getEl();
        int    index = parseContext.getIndex();
        char   c     = el.charAt(index);
        if (c == '-')
        {
            if (CharType.isDigital(el.charAt(index + 1)))
            {
                int preIndex = index - 1;
                while (CharType.isIgnore(el.charAt(preIndex)))
                {
                    preIndex -= 1;
                }
                if (CharType.isDigital(el.charAt(preIndex)))
                {
                    return false;
                }
                else
                {
                    extractNum(parseContext);
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        else if (CharType.isDigital(c))
        {
            extractNum(parseContext);
            return true;
        }
        else
        {
            return false;
        }
    }

    private static void extractNum(ParseContext parseContext)
    {
        int    index = parseContext.getIndex();
        String el    = parseContext.getEl();
        index += 1;
        while (index < el.length() && (CharType.isDigital(el.charAt(index)) || el.charAt(index) == '.'))
        {
            index++;
        }
        String numStr = el.substring(parseContext.getIndex(), index);
        parseContext.getOperandStack().push(new NumberOperand(numStr, el.substring(0, index)));
        parseContext.setIndex(index);
    }
}
