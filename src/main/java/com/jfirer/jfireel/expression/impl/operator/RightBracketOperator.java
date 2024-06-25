package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.PlaceHolder;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;

import java.util.Deque;

public class RightBracketOperator implements Operator
{
    @Override
    public int priority()
    {
        return 0;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        Deque<Operator> operatorStack = parseContext.getOperatorStack();
        while (operatorStack.peek() instanceof ContainerLeftBracketOperator == false && operatorStack.peek() instanceof ArrayLeftAngleOperator == false)
        {
            operatorStack.pop().onPop(parseContext);
        }
        Deque<Operand> processStack = parseContext.getProcessStack();
        Deque<Operand> operandStack = parseContext.getOperandStack();
        while (operandStack.peek() != PlaceHolder.LeftBracketPlaceHolder)
        {
            processStack.push(operandStack.pop());
        }
        operandStack.pop();
        operatorStack.pop().onPop(parseContext);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        throw new UnsupportedOperationException();
    }
}
