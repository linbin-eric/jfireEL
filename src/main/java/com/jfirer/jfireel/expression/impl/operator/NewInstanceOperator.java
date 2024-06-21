package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;

public class NewInstanceOperator implements Operator
{
    @Override
    public int priority()
    {
        return -1;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        parseContext.getOperatorStack().push(this);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
    }
}
