package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

@Data
public abstract class CallOperand implements Operand
{
    protected Operand[] args;
}
