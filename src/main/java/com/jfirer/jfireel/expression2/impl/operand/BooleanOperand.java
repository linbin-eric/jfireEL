package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class BooleanOperand implements Operand
{
    private final boolean value;

    @Override
    public Object calculate(Map<String, Object> param)
    {
        return value;
    }
}
