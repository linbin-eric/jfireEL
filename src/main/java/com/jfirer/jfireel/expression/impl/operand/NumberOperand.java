package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;

import java.util.Map;

public class NumberOperand implements Operand
{
    private       String fragment;
    private final Object value;

    public NumberOperand(String numStr, String fragment)
    {
        this.fragment = fragment;
        Object tmp;
        if (numStr.indexOf('.') != -1)
        {
            try
            {
                tmp = Float.valueOf(numStr);
            }
            catch (Throwable ignore)
            {
                tmp = Double.valueOf(numStr);
            }
        }
        else
        {
            try
            {
                tmp = Integer.valueOf(numStr);
            }
            catch (Throwable ignore)
            {
                tmp = Long.valueOf(numStr);
            }
        }
        value = tmp;
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return value;
    }

    @Override
    public void clearFragment()
    {
        fragment = null;
    }
}
