package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.ArrayOperand;

public class ArrayLeftOperator implements Operator
{
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
        parseContext.getOperatorStack().push(this);
        parseContext.getOperandStack().push(new ArrayOperand());
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
    }
}
