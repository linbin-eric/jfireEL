package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;

import java.util.Map;

public class NullOperand implements Operand
{
    @Override
    public Object calculate(Map<String, Object> param)
    {
        return null;
    }
}
