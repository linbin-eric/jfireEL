package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import lombok.Data;

import java.util.Deque;

@Data
public class SemicolonOperator implements Operator
{
    private final String fragment;

    @Override
    public int priority()
    {
        return 0;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        Deque<Operator> operatorStack = parseContext.getOperatorStack();
        if (operatorStack.isEmpty() == false && operatorStack.peek() instanceof ReturnOperator)
        {
            if (fragment.trim().endsWith("return"))
            {
                ((ReturnOperator) operatorStack.peek()).setWithValue(false);
            }
        }
        while (!operatorStack.isEmpty() && operatorStack.peek() instanceof  LeftAngleBracketOperator ==false)
        {
            operatorStack.pop().onPop(parseContext);
        }
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
    }
}
