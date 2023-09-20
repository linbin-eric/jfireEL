package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

@Data
public class ContainerOperand implements Operand
{
    private final Operand container;
    private final Operand index;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Object instance = container.calculate(contextParam);
        if (instance instanceof List<?> list)
        {
            Number calculate = (Number) index.calculate(contextParam);
            return list.get(calculate.intValue());
        }
        else if (instance instanceof Map<?, ?> map)
        {
            Object key = index.calculate(contextParam);
            return map.get(key);
        }
        else
        {
            Number calculate = (Number) index.calculate(contextParam);
            return Array.get(instance, calculate.intValue());
        }
    }
}
