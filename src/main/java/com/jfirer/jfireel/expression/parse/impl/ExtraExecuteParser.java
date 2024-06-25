package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.*;
import com.jfirer.jfireel.expression.impl.operator.*;
import com.jfirer.jfireel.expression.parse.TokenParser;

public class ExtraExecuteParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        int    index = parseContext.getIndex();
        String el    = parseContext.getEl();
        if (el.charAt(index) == 'v' && index + 4 < el.length() && el.startsWith("var ", index))
        {
            new VarOperator().push(parseContext);
            parseContext.setIndex(index + 3);
            return true;
        }
        else if (el.charAt(index) == 'i' && index + 3 < el.length() && el.startsWith("in ", index))
        {
            new InOperator().push(parseContext);
            parseContext.setIndex(index + 2);
            return true;
        }
        else if (el.charAt(index) == 'i' && index + 2 < el.length() && el.startsWith("if", index))
        {
            parseContext.getOperandStack().push(new IfOperand());
            parseContext.setIndex(index + 2);
            return true;
        }
        else if (el.charAt(index) == 'e' && index + 7 < el.length() && el.startsWith("else if", index))
        {
            parseContext.getOperandStack().push(new ElseIfOperand());
            parseContext.setIndex(index + 7);
            return true;
        }
        else if (el.charAt(index) == 'e' && index + 4 < el.length() && el.startsWith("else", index))
        {
            parseContext.getOperandStack().push(new ElseOperand());
            parseContext.setIndex(index + 4);
            return true;
        }
        else if (el.charAt(index) == 'f' && index + 3 < el.length() && el.startsWith("for", index))
        {
            parseContext.getOperandStack().push(new ForOperand(el.substring(0, index + 3)));
            parseContext.setIndex(index + 3);
            return true;
        }
        else if (el.charAt(index) == 'r' && index + 6 < el.length() && el.startsWith("return", index))
        {
            new ReturnOperator(el.substring(0, index)).push(parseContext);
            parseContext.setIndex(index + 6);
            parseContext.setHasReturnToken(true);
            return true;
        }
        else if (el.charAt(index) == 'b' && index + 5 < el.length() && el.startsWith("break", index))
        {
            parseContext.getOperandStack().push(ControlFlagOperand.BREAK_OPERAND);
            parseContext.setIndex(index + 5);
            return true;
        }
        else if (el.charAt(index) == 'c' && index + 8 < el.length() && el.startsWith("continue", index))
        {
            parseContext.getOperandStack().push(ControlFlagOperand.CONTINUE_OPERAND);
            parseContext.setIndex(index + 8);
            return true;
        }
        else if (el.charAt(index) == 'n' && index + 4 < el.length() && el.startsWith("new ", index))
        {
            parseContext.getOperatorStack().push(new NewInstanceOperator(el.substring(0, index + 4)));
            parseContext.setIndex(index + 4);
            return true;
        }
        else if (el.charAt(index) == '[' && index + 3 < el.length() && el.startsWith("[]{", index))
        {
            new ArrayLeftAngleOperator(el.substring(0, index + 3)).push(parseContext);
            parseContext.setIndex(index + 3);
            return true;
        }
        else if (el.charAt(index) == '{')
        {
            new MethodStructLeftAngleBracketOperator(el.substring(0, index + 1)).push(parseContext);
            parseContext.setIndex(index + 1);
            return true;
        }
        else
        {
            return false;
        }
    }
}
