package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.TokenType;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;
import com.jfirer.jfireel.expression.parse.TokenParser;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class StringSetParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        int    index = parseContext.getIndex();
        String el    = parseContext.getEl();
        if (index + 1 < el.length() && el.charAt(index) == '{' && el.charAt(index + 1) == '\'')
        {
            int endIndex = index + 2;
            while (el.charAt(endIndex) != '}' && endIndex < el.length())
            {
                endIndex++;
            }
            if (endIndex == el.length())
            {
                throw new IllegalArgumentException("使用字符串集合{''},未能发现闭合，检查:" + el.substring(0, index));
            }
            else
            {
                Set<String> collect = Arrays.stream(el.substring(index + 2, endIndex - 1).split(",")).collect(Collectors.toSet());
                parseContext.getOperandStack().push(new VariableOperand(el.substring(parseContext.getIndex(), index)));
                parseContext.setIndex(endIndex + 1);
                parseContext.setLastToken(TokenType.OPERAND);
                return true;
            }
        }
        else
        {
            return false;
        }
    }
}
