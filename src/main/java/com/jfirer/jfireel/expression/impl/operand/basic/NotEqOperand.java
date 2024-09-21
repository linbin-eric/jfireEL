package com.jfirer.jfireel.expression.impl.operand.basic;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.basic.math.EqOperand;

import java.util.Map;

public class NotEqOperand extends BasicOperandImpl
{
    private EqOperand eqOperand;

    public NotEqOperand(String operator, Operand left, Operand right, String fragment)
    {
        super(operator, left, right, fragment);
        eqOperand = new EqOperand(operator, left, right, fragment);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return !(Boolean) eqOperand.calculate(contextParam);
    }
}
