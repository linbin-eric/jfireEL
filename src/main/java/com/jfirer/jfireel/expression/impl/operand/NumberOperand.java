package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;

import java.util.Map;

public class NumberOperand implements Operand
{
    private       Long    lValue;
    private       Double  dValue;
    private final boolean natural;
    private final String  fragment;

    public NumberOperand(String numStr, String fragment)
    {
        this.fragment = fragment;
        if (numStr.indexOf('.') != -1)
        {
            natural = false;
            dValue  = Double.valueOf(numStr);
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
        //Number 的类型强转不能去除，否则会导致外部收到 Long 数据类型的时候自动变为 Double，引起问题。
        return natural ? ((Number) lValue) : ((Number) dValue);
    }
}
