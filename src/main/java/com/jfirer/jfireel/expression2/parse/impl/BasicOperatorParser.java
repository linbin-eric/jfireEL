package com.jfirer.jfireel.expression2.parse.impl;

import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.*;
import com.jfirer.jfireel.expression2.impl.operator.BasicOperator;
import com.jfirer.jfireel.expression2.parse.TokenParser;

public class BasicOperatorParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        String   el       = parseContext.getEl();
        int      index    = parseContext.getIndex();
        char     c        = el.charAt(index);
        Operator operator = null;
        switch (c)
        {
            case '+' -> operator = new BasicOperator(2, PlusOperand::new);
            case '-' -> operator = new BasicOperator(2, MinusOperand::new);
            case '*' -> operator = new BasicOperator(3, MutliOperand::new);
            case '/' -> operator = new BasicOperator(3, DivisionOperand::new);
            case '%' -> operator = new BasicOperator(3, RemainOperand::new);
        }
        if (operator != null)
        {
            operator.push(parseContext);
            parseContext.setIndex(index + 1);
            return true;
        }
        switch (c)
        {
            case '>' ->
            {
                if (el.charAt(index + 1) == '=')
                {
                    new BasicOperator(1, GeOperand::new).push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    new BasicOperator(1, GtOperand::new).push(parseContext);
                    parseContext.setIndex(index + 1);
                }
                return true;
            }
            case '<' ->
            {
                if (el.charAt(index + 1) == '=')
                {
                    new BasicOperator(1, LeOperand::new).push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    new BasicOperator(1, LtOperand::new).push(parseContext);
                    parseContext.setIndex(index + 1);
                }
                return true;
            }
            case '=' ->
            {
                if (el.charAt(index + 1) == '=')
                {
                    new BasicOperator(1, EqOperand::new).push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    throw new IllegalArgumentException("解析出现异常，当前解析位置:" + el.substring(0, index));
                }
                return true;
            }
            case '!' ->
            {
                if (el.charAt(index + 1) == '=')
                {
                    new BasicOperator(1, NotEqOperand::new).push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    throw new IllegalArgumentException("解析出现异常，当前解析位置:" + el.substring(0, index));
                }
                return true;
            }
            case '&' ->
            {
                if (el.charAt(index + 1) == '&')
                {
                    new BasicOperator(1, AndOperand::new).push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    ;
                }
            }
            case '|' ->
            {
                if (el.charAt(index + 1) == '|')
                {
                    new BasicOperator(1, OrOperand::new).push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    ;
                }
            }
        }
        return false;
    }
}
