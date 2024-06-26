package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class ArrayOperand implements Operand
{
    private final Operand[] array;
    private       boolean   constant;
    private       Object    value;

    public ArrayOperand(Operand[] array)
    {
        this.array = array;
        if (Arrays.stream(array).filter(Objects::nonNull).allMatch(one -> one instanceof LiteralOparand || one instanceof NumberOperand || one instanceof ClassOperand || one instanceof PropertyReadOperand.StaticClassPropertyOperand|| one instanceof  NullOperand))
        {
            constant = true;
            Object[] instance = new Object[array.length];
            for (int i = 0; i < instance.length; i++)
            {
                instance[i] = array[i].calculate(null);
            }
            if (Arrays.stream(instance).filter(Objects::nonNull).allMatch(one -> one instanceof String))
            {
                value = Arrays.stream(instance).map(one -> ((String) one)).toArray(String[]::new);
            }
            else if (Arrays.stream(instance).filter(Objects::nonNull).allMatch(one -> one instanceof Number))
            {
                value = Arrays.stream(instance).map(one -> ((Number) one)).toArray(Number[]::new);
            }
            else if (Arrays.stream(instance).filter(Objects::nonNull).map(Object::getClass).collect(Collectors.toSet()).size() == 1)
            {
                Class<?> ckass = Arrays.stream(instance).filter(Objects::nonNull).findAny().get().getClass();
                Object   o     = Array.newInstance(ckass, instance.length);
                for (int i = 0; i < instance.length; i++)
                {
                    Array.set(o, i, instance[i]);
                }
                value = o;
            }
            else
            {
                value = instance;
            }
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
        Object[] instance = new Object[array.length];
        for (int i = 0; i < instance.length; i++)
        {
            instance[i] = array[i].calculate(contextParam);
        }
        return instance;
    }
}
