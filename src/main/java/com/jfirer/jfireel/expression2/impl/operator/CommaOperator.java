package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;

public class CommaOperator implements Operator
{
    @Override
    public int priority()
    {
        return -1;
    }

    @Override
    public void push(ParseContext parseContext)
    {
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
    }
}
