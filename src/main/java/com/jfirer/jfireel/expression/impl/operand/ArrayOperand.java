package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class ArrayOperand implements Operand
{
    private final Operand[] array;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Object[] instance = new Object[array.length];
        for (int i = 0; i < instance.length; i++)
        {
            instance[i] = array[i].calculate(contextParam);
        }
        return instance;
    }
}
