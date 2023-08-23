package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operand;
import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.InOperand;
import com.jfirer.jfireel.expression2.impl.operand.VariableOperand;

import java.util.Deque;

public class InOperator implements Operator
{
    @Override
    public int priority()
    {
        return -2;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        parseContext.getOperatorStack().push(this);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Deque<Operand>  operandStack   = parseContext.getOperandStack();
        Operand         itemsContainer = operandStack.pop();
        VariableOperand itemName       = (VariableOperand) operandStack.pop();
        operandStack.push(new InOperand(itemName.getVariable(), itemsContainer));
    }
}
