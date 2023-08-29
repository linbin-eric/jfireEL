package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import lombok.Data;

import java.util.Deque;

@Data
public class CommaOperator implements Operator
{
    private final String fragment;

    @Override
    public int priority()
    {
        return -1;
    }

    @Override
    public boolean isBoundary()
    {
        return true;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        Deque<Operator> operatorStack = parseContext.getOperatorStack();
        do
        {
            Operator peek = operatorStack.peek();
            if (peek instanceof LeftParenOperator || peek instanceof CommaOperator)
            {
                break;
            }
            else
            {
                Operator pop = operatorStack.pop();
                pop.onPop(parseContext);
            }
        } while (true);
        parseContext.getOperatorStack().push(this);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        parseContext.getProcessStack().push(parseContext.getOperandStack().pop());
    }
}
