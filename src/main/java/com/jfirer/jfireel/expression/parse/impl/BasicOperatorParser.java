package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.ELConfig;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.basic.AndOperand;
import com.jfirer.jfireel.expression.impl.operand.basic.NotEqOperand;
import com.jfirer.jfireel.expression.impl.operand.basic.math.compile.CompileMathOperand;
import com.jfirer.jfireel.expression.impl.operand.basic.math.standard.*;
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
            case '+' -> operator = new BasicOperator("+", 2, fragment, (elConfig.isMathUseCompile() || elConfig.isPlusUseCompile()) ? CompileMathOperand::new : PlusOperand::new);
            case '-' -> operator = new BasicOperator("-", 2, fragment, (elConfig.isMathUseCompile() || elConfig.isMinusUseCompile()) ? CompileMathOperand::new : MinusOperand::new);
            case '*' -> operator = new BasicOperator("*", 3, fragment, (elConfig.isMathUseCompile() || elConfig.isMultiplyUseCompile()) ? CompileMathOperand::new : MultiplyOperand::new);
            case '/' -> operator = new BasicOperator("/", 3, fragment, (elConfig.isMathUseCompile() || elConfig.isDivideUseCompile()) ? CompileMathOperand::new : DivisionOperand::new);
            case '%' -> operator = new BasicOperator("%", 3, fragment, (elConfig.isMathUseCompile() || elConfig.isRemainUseCompile()) ? CompileMathOperand::new : RemainOperand::new);
            case '?' -> operator = new QuestionOperator();
            case ':' -> operator = new ColonOperator(fragment);
            case '.' -> operator = new SpotOperator(fragment);
            case ',' -> operator = new CommaOperator(fragment);
            case '[' -> operator = new LeftBracketOperator(fragment);
            case ']' -> operator = new RightBracketOperator(fragment);
            case ';' -> operator = new SemicolonOperator(fragment);
            case '{' -> operator = new LeftBraceOperator(fragment);
            case '}' -> operator = new RightBraceOperator(fragment);
        }
        if (operator != null)
        {
            operator.push(parseContext);
            parseContext.setIndex(index + 1);
            parseContext.getRecognizeToken().add(operator);
            return true;
        }
        switch (c)
        {
            case '>' ->
            {
                if (index + 1 < el.length() && el.charAt(index + 1) == '=')
                {
                    operator = new BasicOperator(">=", 1, fragment, (elConfig.isMathUseCompile() || elConfig.isGeUseCompile()) ? CompileMathOperand::new : GeOperand::new);
                    operator.push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else if (index + 1 < el.length() && el.charAt(index + 1) == '>')
                {
                    operator = new SetEndOperator(fragment);
                    operator.push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    operator = new BasicOperator(">", 1, fragment, (elConfig.isMathUseCompile() || elConfig.isGtUseCompile()) ? CompileMathOperand::new : GtOperand::new);
                    operator.push(parseContext);
                    parseContext.setIndex(index + 1);
                }
                parseContext.getRecognizeToken().add(operator);
                return true;
            }
            case '<' ->
            {
                if (index + 1 < el.length() && el.charAt(index + 1) == '=')
                {
                    operator = new BasicOperator("<=", 1, fragment, (elConfig.isMathUseCompile() || elConfig.isLeUseCompile()) ? CompileMathOperand::new : LeOperand::new);
                    operator.push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else if (index + 1 < el.length() && el.charAt(index + 1) == '<')
                {
                    operator = new SetStartOperator(fragment);
                    operator.push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    operator = new BasicOperator("<", 1, fragment, (elConfig.isMathUseCompile() || elConfig.isLtUseCompile()) ? CompileMathOperand::new : LtOperand::new);
                    operator.push(parseContext);
                    parseContext.setIndex(index + 1);
                }
                parseContext.getRecognizeToken().add(operator);
                return true;
            }
            case '=' ->
            {
                if (index + 1 < el.length() && el.charAt(index + 1) == '=')
                {
                    operator = new BasicOperator("==", 1, fragment, (elConfig.isMathUseCompile() || elConfig.isEqUseCompile()) ? CompileMathOperand::new : EqOperand::new);
                    operator.push(parseContext);
                    parseContext.setIndex(index + 2);
                }
                else
                {
                    operator = new AssignOperator(fragment);
                    operator.push(parseContext);
                    parseContext.setIndex(index + 1);
                }
                parseContext.getRecognizeToken().add(operator);
                return true;
            }
            case '!' ->
            {
                if (index + 1 < el.length() && el.charAt(index + 1) == '=')
                {
                    operator = new BasicOperator("!=", 1, fragment, NotEqOperand::new);
                    operator.push(parseContext);
                    parseContext.setIndex(index + 2);
                    parseContext.getRecognizeToken().add(operator);
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
                    operator = new BasicOperator("&&", 0, fragment, AndOperand::new);
                    operator.push(parseContext);
                    parseContext.setIndex(index + 2);
                    parseContext.getRecognizeToken().add(operator);
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
                    operator = new BasicOperator("||", 0, fragment, OrOperand::new);
                    operator.push(parseContext);
                    parseContext.setIndex(index + 2);
                    parseContext.getRecognizeToken().add(operator);
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
