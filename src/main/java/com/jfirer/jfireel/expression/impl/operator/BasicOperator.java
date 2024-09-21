package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.TokenType;

import java.util.Deque;

public class BasicOperator implements Operator
{
    private String       operator;
    private int          priority;
    private String       fragment;
    private BasicOperand constructor;

    @FunctionalInterface
    public interface BasicOperand
    {
        Operand apply(String operator, Operand left, Operand right, String fragment);
    }

    public BasicOperator(String operator, int priority, String fragment, BasicOperand constructor)
    {
        this.operator    = operator;
        this.priority    = priority;
        this.fragment    = fragment;
        this.constructor = constructor;
    }

    @Override
    public int priority()
    {
        return priority;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        Deque<Operator> operatorStack = parseContext.getOperatorStack();
        while (operatorStack.isEmpty() == false && operatorStack.peek().priority() >= priority())
        {
            operatorStack.pop().onPop(parseContext);
        }
        operatorStack.push(this);
        parseContext.setLastToken(TokenType.OPERATOR);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Deque<Operand> operandStack = parseContext.getOperandStack();
        Operand        right        = operandStack.pop();
        Operand        left         = operandStack.pop();
        operandStack.push(constructor.apply(operator, left, right, fragment));
    }
}
