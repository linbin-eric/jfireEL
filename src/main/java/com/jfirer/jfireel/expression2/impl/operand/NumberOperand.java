package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;

import java.util.Map;

public class NumberOperand implements Operand
{
    private       Long    lValue;
    private       Double  dValuel;
    private final boolean natural;

    public NumberOperand(String numStr)
    {
        if (numStr.indexOf('.') != -1)
        {
            natural = false;
            dValuel = Double.valueOf(numStr);
        }
        else
        {
            natural = true;
            lValue  = Long.valueOf(numStr);
        }
    }

    @Override
    public Object calculate(Map<String, Object> param)
    {
        return natural ? lValue : dValuel;
    }
}
