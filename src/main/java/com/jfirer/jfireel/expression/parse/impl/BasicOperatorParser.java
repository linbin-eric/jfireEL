package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.BasicOperandImpl;
import com.jfirer.jfireel.expression.impl.operator.*;
import com.jfirer.jfireel.expression.parse.TokenParser;

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
            case '*' -> operator = new BasicOperator(3, fragment, BasicOperandImpl.TimesOperand::new);
            case '/' -> operator = new BasicOperator(3, fragment, BasicOperandImpl.DivisionOperand::new);
            case '%' -> operator = new BasicOperator(3, fragment, BasicOperandImpl.RemainOperand::new);
            case '?' -> operator = new QuestionOperator();
            case ':' -> operator = new ColonOperator(fragment);
            case '.' -> operator = new SpotOperator(fragment);
            case ',' -> operator = new CommaOperator(fragment);
            case '[' -> operator = new ContainerLeftBracketOperator(fragment);
            case ']' -> operator = new RightBracketOperator();
            case ';' -> operator = new SemicolonOperator(el.substring(0, index));
            case '}' -> operator = new RightAngleBracketOperator(fragment);
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
                if (index + 1 < el.length() && el.charAt(index + 1) == '=')
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
                if (index + 1 < el.length() && el.charAt(index + 1) == '=')
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
                if (index + 1 < el.length() && el.charAt(index + 1) == '=')
                {
                    new BasicOperator(1, fragment, BasicOperandImpl.EqOperand::new).push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    new AssignOperator(fragment).push(parseContext);
                    parseContext.setIndex(index + 1);
                }
                return true;
            }
            case '!' ->
            {
                if (index + 1 < el.length() && el.charAt(index + 1) == '=')
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
                if (index + 1 < el.length() && el.charAt(index + 1) == '&')
                {
                    new BasicOperator(0, fragment, BasicOperandImpl.AndOperand::new).push(parseContext);
                    parseContext.setIndex(index + 2);
                    return true;
                }
                else
                {
                    ;
                }
            }
            case '|' ->
            {
                if (index + 1 < el.length() && el.charAt(index + 1) == '|')
                {
                    new BasicOperator(0, fragment, BasicOperandImpl.OrOperand::new).push(parseContext);
                    parseContext.setIndex(index + 2);
                    return true;
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
