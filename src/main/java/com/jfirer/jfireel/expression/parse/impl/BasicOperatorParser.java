package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.ELConfig;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.TokenType;
import com.jfirer.jfireel.expression.impl.operand.basic.AndOperand;
import com.jfirer.jfireel.expression.impl.operand.basic.NotEqOperand;
import com.jfirer.jfireel.expression.impl.operand.basic.math.*;
import com.jfirer.jfireel.expression.impl.operator.*;
import com.jfirer.jfireel.expression.parse.TokenParser;

public class BasicOperatorParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        ELConfig elConfig = parseContext.getConfig();
        String   el       = parseContext.getEl();
        int      index    = parseContext.getIndex();
        char     c        = el.charAt(index);
        Operator operator = null;
        String   fragment = el.substring(0, index);
        switch (c)
        {
            case '+' -> operator = new BasicOperator("+",2, fragment, PlusOperand::new);
            case '-' -> operator = new BasicOperator("-",2, fragment, elConfig.isMinusUseCompile() ? CompileMathOperand::new : MinusOperand::new);
            case '*' -> operator = new BasicOperator("*",3, fragment, TimesOperand::new);
            case '/' -> operator = new BasicOperator("/",3, fragment, DivisionOperand::new);
            case '%' -> operator = new BasicOperator("%",3, fragment, RemainOperand::new);
            case '?' -> operator = new QuestionOperator();
            case ':' -> operator = new ColonOperator(fragment);
            case '.' -> operator = new SpotOperator(fragment);
            case ',' -> operator = new CommaOperator(fragment);
            case '[' -> operator = new LeftBracketOperator(fragment);
            case ']' -> operator = new AbstractRightOperator.RightBracketOperator(fragment);
            case ';' -> operator = new SemicolonOperator(fragment);
            case '{' -> operator = new LeftAngleBracketOperator(fragment);
            case '}' -> operator = new AbstractRightOperator.RightAngleBracketOperator(fragment);
        }
        if (operator != null)
        {
            operator.push(parseContext);
            parseContext.setIndex(index + 1);
            parseContext.setLastToken(TokenType.OPERATOR);
            return true;
        }
        switch (c)
        {
            case '>' ->
            {
                if (index + 1 < el.length() && el.charAt(index + 1) == '=')
                {
                    new BasicOperator(">=",1, fragment, GeOperand::new).push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    new BasicOperator(">",1, fragment, GtOperand::new).push(parseContext);
                    parseContext.setIndex(index + 1);
                }
                return true;
            }
            case '<' ->
            {
                if (index + 1 < el.length() && el.charAt(index + 1) == '=')
                {
                    new BasicOperator("<=",1, fragment, LeOperand::new).push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    new BasicOperator("<",1, fragment, LtOperand::new).push(parseContext);
                    parseContext.setIndex(index + 1);
                }
                return true;
            }
            case '=' ->
            {
                if (index + 1 < el.length() && el.charAt(index + 1) == '=')
                {
                    new BasicOperator("==",1, fragment, EqOperand::new).push(parseContext);
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
                    new BasicOperator("!=",1, fragment, NotEqOperand::new).push(parseContext);
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
                    new BasicOperator("&&",0, fragment, AndOperand::new).push(parseContext);
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
                    new BasicOperator("||",0, fragment, OrOperand::new).push(parseContext);
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
