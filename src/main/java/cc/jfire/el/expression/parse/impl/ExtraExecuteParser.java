package cc.jfire.el.expression.parse.impl;

import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operand.*;
import cc.jfire.el.expression.impl.operator.InOperator;
import cc.jfire.el.expression.impl.operator.NewInstanceOperator;
import cc.jfire.el.expression.impl.operator.ReturnOperator;
import cc.jfire.el.expression.impl.operator.VarOperator;
import cc.jfire.el.expression.parse.TokenParser;

public class ExtraExecuteParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        int    index = parseContext.getIndex();
        String el    = parseContext.getEl();
        if (el.charAt(index) == 'v' && index + 4 < el.length() && el.startsWith("var ", index))
        {
            VarOperator varOperator = new VarOperator();
            varOperator.push(parseContext);
            parseContext.setIndex(index + 3);
            parseContext.getRecognizeToken().add(varOperator);
            return true;
        }
        else if (el.charAt(index) == 'i' && index + 3 < el.length() && el.startsWith("in ", index))
        {
            InOperator inOperator = new InOperator();
            inOperator.push(parseContext);
            parseContext.setIndex(index + 2);
            parseContext.getRecognizeToken().add(inOperator);
            return true;
        }
        else if (el.charAt(index) == 'i' && index + 2 < el.length() && el.startsWith("if", index))
        {
            IfOperand ifOperand = new IfOperand();
            parseContext.getOperandStack().push(ifOperand);
            parseContext.setIndex(index + 2);
            parseContext.getRecognizeToken().add(ifOperand);
            return true;
        }
        else if (el.charAt(index) == 'e' && index + 7 < el.length() && el.startsWith("else if", index))
        {
            ElseIfOperand elseIfOperand = new ElseIfOperand();
            parseContext.getOperandStack().push(elseIfOperand);
            parseContext.setIndex(index + 7);
            parseContext.getRecognizeToken().add(elseIfOperand);
            return true;
        }
        else if (el.charAt(index) == 'e' && index + 4 < el.length() && el.startsWith("else", index))
        {
            ElseOperand elseOperand = new ElseOperand();
            parseContext.getOperandStack().push(elseOperand);
            parseContext.setIndex(index + 4);
            parseContext.getRecognizeToken().add(elseOperand);
            return true;
        }
        else if (el.charAt(index) == 'f' && index + 3 < el.length() && el.startsWith("for", index))
        {
            ForOperand forOperand = new ForOperand(el.substring(0, index + 3));
            parseContext.getOperandStack().push(forOperand);
            parseContext.setIndex(index + 3);
            parseContext.getRecognizeToken().add(forOperand);
            return true;
        }
        else if (el.charAt(index) == 'r' && index + 6 < el.length() && el.startsWith("return", index))
        {
            ReturnOperator returnOperator = new ReturnOperator(el.substring(0, index));
            returnOperator.push(parseContext);
            parseContext.setIndex(index + 6);
            parseContext.setHasReturnToken(true);
            parseContext.getRecognizeToken().add(returnOperator);
            return true;
        }
        else if (el.charAt(index) == 'b' && index + 5 < el.length() && el.startsWith("break", index))
        {
            parseContext.getOperandStack().push(ControlFlagOperand.BREAK_OPERAND);
            parseContext.setIndex(index + 5);
            parseContext.getRecognizeToken().add(ControlFlagOperand.BREAK_OPERAND);
            return true;
        }
        else if (el.charAt(index) == 'c' && index + 8 < el.length() && el.startsWith("continue", index))
        {
            parseContext.getOperandStack().push(ControlFlagOperand.CONTINUE_OPERAND);
            parseContext.setIndex(index + 8);
            parseContext.getRecognizeToken().add(ControlFlagOperand.CONTINUE_OPERAND);
            return true;
        }
        else if (el.charAt(index) == 'n' && index + 4 < el.length() && el.startsWith("new ", index))
        {
            NewInstanceOperator newInstanceOperator = new NewInstanceOperator(el.substring(0, index + 4));
            parseContext.getOperatorStack().push(newInstanceOperator);
            parseContext.setIndex(index + 4);
            parseContext.getRecognizeToken().add(newInstanceOperator);
            return true;
        }
        else
        {
            return false;
        }
    }
}
