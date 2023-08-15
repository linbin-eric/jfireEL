package com.jfirer.jfireel.expression2.parse.impl;

import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.BasicOperandImpl;
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
        String   fragment = el.substring(0, index);
        switch (c)
        {
            case '+' -> operator = new BasicOperator(2, fragment, BasicOperandImpl.PlusOperand::new);
            case '-' -> operator = new BasicOperator(2, fragment, BasicOperandImpl.MinusOperand::new);
            case '*' -> operator = new BasicOperator(3, fragment, BasicOperandImpl.MutliOperand::new);
            case '/' -> operator = new BasicOperator(3, fragment, BasicOperandImpl.DivisionOperand::new);
            case '%' -> operator = new BasicOperator(3, fragment, BasicOperandImpl.RemainOperand::new);
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
                    new BasicOperator(1, fragment, BasicOperandImpl.GeOperand::new).push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    new BasicOperator(1, fragment, BasicOperandImpl.GtOperand::new).push(parseContext);
                    parseContext.setIndex(index + 1);
                }
                return true;
            }
            case '<' ->
            {
                if (el.charAt(index + 1) == '=')
                {
                    new BasicOperator(1, fragment, BasicOperandImpl.LeOperand::new).push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    new BasicOperator(1, fragment, BasicOperandImpl.LtOperand::new).push(parseContext);
                    parseContext.setIndex(index + 1);
                }
                return true;
            }
            case '=' ->
            {
                if (el.charAt(index + 1) == '=')
                {
                    new BasicOperator(1, fragment, BasicOperandImpl.EqOperand::new).push(parseContext);
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
                    new BasicOperator(1, fragment, BasicOperandImpl.NotEqOperand::new).push(parseContext);
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
                    new BasicOperator(1, fragment, BasicOperandImpl.AndOperand::new).push(parseContext);
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
                    new BasicOperator(1, fragment, BasicOperandImpl.OrOperand::new).push(parseContext);
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
