package com.jfirer.jfireel.expression.impl.operand.property;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

@Data
public abstract class PropertyReadOperand implements Operand
{
    protected final Operand                              typeOperand;
    protected final VariableOperand                      propertyNameOperand;
    protected final String                               fragment;
    protected final Map<Field, Function<Object, Object>> propertyReadAccelerators;

    public static Field findField(Class<?> ckass, String fieldName, String fragment)
    {
        while (ckass != Object.class)
        {
            try
            {
                return ckass.getDeclaredField(fieldName);
            }
            catch (NoSuchFieldException e)
            {
                ckass = ckass.getSuperclass();
            }
        }
        throw new IllegalArgumentException("解析属性，未能发现属性，异常解析表达式位置为：" + fragment);
    }
}
