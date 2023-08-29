package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;

import java.util.Map;

public class NullOperand implements Operand
{
    @Override
    public Object calculate(Map<String, Object> param)
    {
        return null;
    }
}
