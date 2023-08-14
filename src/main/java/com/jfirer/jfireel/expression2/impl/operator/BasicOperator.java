package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operand;
import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;

import java.util.Deque;
import java.util.function.BiFunction;

public class BasicOperator implements Operator
{
    private int                                   priority;
    private BiFunction<Operand, Operand, Operand> constructor;

    public BasicOperator(int priority, BiFunction<Operand, Operand, Operand> constructor)
    {
        this.priority    = priority;
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
        operandStack.push(constructor.apply(left, right));
    }
}
