package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.ControlFlagOperand;
import com.jfirer.jfireel.expression.impl.operand.ReturnWithValueOperand;
import lombok.Data;

import java.util.Deque;

@Data
public class ReturnOperator implements Operator
{
    private final String  fragment;
    private       boolean withValue = true;

    @Override
    public int priority()
    {
        return -3;
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
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Deque<Operand> operandStack = parseContext.getOperandStack();
        if (withValue)
        {
            parseContext.getOperandStack().push(new ReturnWithValueOperand(operandStack.pop()));
        }
        else
        {
            parseContext.getOperandStack().push(ControlFlagOperand.RETURN_OPERAND);
        }
    }
}
