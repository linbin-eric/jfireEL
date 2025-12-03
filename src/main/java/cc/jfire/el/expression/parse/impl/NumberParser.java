package cc.jfire.el.expression.parse.impl;

import cc.jfire.el.expression.CharType;
import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operand.NumberOperand;
import cc.jfire.el.expression.parse.TokenParser;

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
                while (preIndex != -1 && CharType.isIgnore(el.charAt(preIndex)))
                {
                    preIndex -= 1;
                }
                if (preIndex == -1)
                {
                    extractNum(parseContext);
                    return true;
                }
                c = el.charAt(preIndex);
                if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^' || c == '(' || c == '[' || c == '{' || c == '=' || c == '?' || c == ':')
                {
                    extractNum(parseContext);
                    return true;
                }
                else
                {
                    return false;
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
        String        numStr        = el.substring(parseContext.getIndex(), index);
        NumberOperand numberOperand = new NumberOperand(numStr, el.substring(0, index));
        parseContext.getOperandStack().push(numberOperand);
        parseContext.setIndex(index);
        parseContext.getRecognizeToken().add(numberOperand);
    }
}
