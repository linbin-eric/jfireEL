package com.jfirer.jfireel.expression;

import java.util.Map;

public interface Operand
{
    Object calculate(Map<String, Object> param);

    default Object calculate()
    {
        return calculate(null);
    }
}
