package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;

import java.util.Map;

public class NotEqOperand implements Operand
{
    private EqOperand eqOperand;

    public NotEqOperand(Operand left, Operand right)
    {
        eqOperand = new EqOperand(left, right);
    }

    @Override
    public Object calculate(Map<String, Object> param)
    {
        return ((Boolean) eqOperand.calculate(param)) == false;
    }
}
