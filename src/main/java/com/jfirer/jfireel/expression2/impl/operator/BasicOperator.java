package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operand;
import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;

import java.util.Deque;
import java.util.function.BiFunction;

public class BasicOperator implements Operator
{
    private int          priority;
    private String       fragment;
    private BasicOperand constructor;

    @FunctionalInterface
    public interface BasicOperand
    {
        Operand apply(Operand left, Operand right, String fragment);
    }

    public BasicOperator(int priority, String fragment, BasicOperand constructor)
    {
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
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Deque<Operand> operandStack = parseContext.getOperandStack();
        Operand        right        = operandStack.pop();
        Operand        left         = operandStack.pop();
        operandStack.push(constructor.apply(left, right, fragment));
    }
}
