package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.CreateVariableOperand;
import com.jfirer.jfireel.expression2.impl.operand.VariableOperand;

public class VarOperator implements Operator
{
    @Override
    public int priority()
    {
        return 11;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        parseContext.getOperatorStack().push(this);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        VariableOperand pop = (VariableOperand) parseContext.getOperandStack().pop();
        parseContext.getOperandStack().push(new CreateVariableOperand(pop.getVariable()));
    }
}
