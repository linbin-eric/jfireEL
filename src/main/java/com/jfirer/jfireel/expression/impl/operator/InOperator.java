package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.InOperand;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;

import java.util.Deque;

public class InOperator implements Operator
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
        Deque<Operand>  operandStack   = parseContext.getOperandStack();
        Operand         itemsContainer = operandStack.pop();
        VariableOperand itemName       = (VariableOperand) operandStack.pop();
        operandStack.push(new InOperand(itemName.getVariable(), itemsContainer));
    }
}
