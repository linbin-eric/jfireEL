package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;

import java.util.Map;
import java.util.Set;

public class StringSetOperand implements Operand
{
    private Set<String> set;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return set;
    }
}
