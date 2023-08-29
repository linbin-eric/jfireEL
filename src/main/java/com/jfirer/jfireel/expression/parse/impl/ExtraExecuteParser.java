package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.*;
import com.jfirer.jfireel.expression.impl.operator.InOperator;
import com.jfirer.jfireel.expression.impl.operator.VarOperator;
import com.jfirer.jfireel.expression.parse.TokenParser;

public class ExtraExecuteParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        int    index = parseContext.getIndex();
        String el    = parseContext.getEl();
        if (el.charAt(index) == 'v' && index + 2 < el.length() && el.charAt(index + 1) == 'a' && el.charAt(index + 2) == 'r')
        {
            new VarOperator().push(parseContext);
            parseContext.setIndex(index + 3);
            return true;
        }
        if (el.charAt(index) == 'i' && index + 1 < el.length() && el.charAt(index + 1) == 'n')
        {
            new InOperator().push(parseContext);
            parseContext.setIndex(index + 2);
            return true;
        }
        else if (el.charAt(index) == '{')
        {
            parseContext.getOperandStack().push(new LeftAngleBracketOperand());
            parseContext.setIndex(index + 1);
            return true;
        }
        else if (el.charAt(index) == 'i' && index + 1 < el.length() && el.charAt(index + 1) == 'f')
        {
            parseContext.getOperandStack().push(new IfOperand());
            parseContext.setIndex(index + 2);
            return true;
        }
        else if (el.charAt(index) == 'e' && index + 7 < el.length() && el.substring(index, index + 7).equals("else if"))
        {
            parseContext.getOperandStack().push(new ElseIfOperand());
            parseContext.setIndex(index + 7);
            return true;
        }
        else if (el.charAt(index) == 'e' && index + 4 < el.length() && el.substring(index, index + 4).equals("else"))
        {
            parseContext.getOperandStack().push(new ElseOperand());
            parseContext.setIndex(index + 4);
            return true;
        }
        else if (el.charAt(index) == 'f' && index + 2 < el.length() && el.charAt(index + 1) == 'o' && el.charAt(index + 2) == 'r')
        {
            parseContext.getOperandStack().push(new ForOperand(el.substring(0, index + 3)));
            parseContext.setIndex(index + 3);
            return true;
        }
        else if (el.charAt(index) == 'r' && index + 6 < el.length() && el.substring(index, index + 6).equals("return"))
        {
            parseContext.getOperandStack().push(ControlFlagOperand.RETURN_OPERAND);
            parseContext.setIndex(index + 6);
            return true;
        }
        else if (el.charAt(index) == 'b' && index + 5 < el.length() && el.substring(index, index + 5).equals("break"))
        {
            parseContext.getOperandStack().push(ControlFlagOperand.BREAK_OPERAND);
            parseContext.setIndex(index + 5);
            return true;
        }
        else if (el.charAt(index) == 'b' && index + 8 < el.length() && el.substring(index, index + 5).equals("continue"))
        {
            parseContext.getOperandStack().push(ControlFlagOperand.CONTINUE_OPERAND);
            parseContext.setIndex(index + 8);
            return true;
        }
        else
        {
            return false;
        }
    }
}
