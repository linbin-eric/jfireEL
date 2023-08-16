package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;

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
        while (operatorStack.peek() instanceof LeftBracketOperator == false)
        {
            operatorStack.pop().onPop(parseContext);
        }
        operatorStack.pop().onPop(parseContext);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        throw new UnsupportedOperationException();
    }
}
