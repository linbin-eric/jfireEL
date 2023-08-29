package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.CreateVariableOperand;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;

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
