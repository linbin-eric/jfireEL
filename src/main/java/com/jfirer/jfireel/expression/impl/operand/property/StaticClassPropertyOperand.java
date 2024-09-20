package com.jfirer.jfireel.expression.impl.operand.property;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.ClassOperand;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class StaticClassPropertyOperand extends PropertyReadOperand
{
    private final Supplier<Object> propertySuppiler;

    public StaticClassPropertyOperand(Operand typeOperand, VariableOperand propertyNameOperand, String fragment, Map<Field, Function<Object, Object>> propertyReadAccelerators)
    {
        super(typeOperand, propertyNameOperand, fragment, propertyReadAccelerators);
        Class<?> ckass = ((ClassOperand) typeOperand).getCkass();
        Field    field = findField(ckass, propertyNameOperand.getVariable(), fragment);
        if (Modifier.isStatic(field.getModifiers()) == false)
        {
            throw new IllegalArgumentException("解析属性异常，非 static 属性不能获取。异常错误位置在:[" + fragment + "]");
        }
        field.setAccessible(true);
        Function<Object, Object> function = propertyReadAccelerators.get(field);
        if (function != null)
        {
            propertySuppiler = () -> function.apply(null);
        }
        else
        {
            propertySuppiler = () -> {
                try
                {
                    return field.get(null);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            };
        }
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return propertySuppiler.get();
    }
}
