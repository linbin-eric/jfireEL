package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.property.StaticClassPropertyOperand;

import java.util.*;
import java.util.stream.Collectors;

public class SetOperand implements Operand
{
    private final Operand[]   array;
    private       boolean     constant;
    private       Set<Object> value;

    public SetOperand(Operand[] array)
    {
        this.array = array;
        if (Arrays.stream(array).filter(Objects::nonNull).allMatch(one -> one instanceof LiteralOparand || one instanceof NumberOperand || one instanceof ClassOperand || one instanceof StaticClassPropertyOperand || one instanceof NullOperand))
        {
            constant = true;
            Object[] instance = new Object[array.length];
            for (int i = 0; i < instance.length; i++)
            {
                instance[i] = array[i].calculate(null);
            }
            value = Arrays.stream(instance).collect(Collectors.toSet());
        }
        else
        {
            constant = false;
        }
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        if (constant)
        {
            return value;
        }
        Set<Object> set = new HashSet<>();
        for (int i = 0; i < array.length; i++)
        {
            set.add(array[i].calculate(contextParam));
        }
        return set;
    }

    @Override
    public void clearFragment()
    {
        for (Operand each : array)
        {
            each.clearFragment();
        }
    }
}
