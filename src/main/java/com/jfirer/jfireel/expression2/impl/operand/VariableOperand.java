package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;

public class VariableOperand implements Operand
{
    private  final String variable;

    public VariableOperand(String variable)
    {
        this.variable = variable;
    }
}
