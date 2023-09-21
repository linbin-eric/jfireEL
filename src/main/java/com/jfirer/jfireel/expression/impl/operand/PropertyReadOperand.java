package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.baseutil.reflect.LambdaValueAccessor;
import com.jfirer.baseutil.reflect.ValueAccessor;
import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Data
public abstract class PropertyReadOperand implements Operand
{
    protected final Operand                              typeOperand;
    protected final VariableOperand                      propertyNameOperand;
    protected final String                               fragment;
    protected final Map<Field, Function<Object, Object>> propertyReadAccelerators;

    protected Field findField(Class<?> ckass, String fieldName, String fragment)
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

    public static class StaticClassPropertyOperand extends PropertyReadOperand
    {
        private final Supplier<Object> propertySuppiler;

        public StaticClassPropertyOperand(Operand typeOperand, VariableOperand propertyNameOperand, String fragment, Map<Field, Function<Object, Object>> propertyReadAccelerators)
        {
            super(typeOperand, propertyNameOperand, fragment, propertyReadAccelerators);
            Class<?> ckass = ((ClassOperand) typeOperand).getCkass();
            Field    field = findField(ckass, propertyNameOperand.getVariable(), fragment);
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

    public static class InstancePropertyReadOperand extends PropertyReadOperand
    {
        private volatile Function<Object, Object> propertyGetter;

        public InstancePropertyReadOperand(Operand typeOperand, VariableOperand propertyNameOperand, String fragment,  Map<Field, Function<Object, Object>> propertyReadAccelerators)
        {
            super(typeOperand, propertyNameOperand, fragment, propertyReadAccelerators);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            if (propertyGetter == null)
            {
                synchronized (this)
                {
                    if (propertyGetter == null)
                    {
                        Object                   instance = typeOperand.calculate(contextParam);
                        Field                    field    = findField(instance.getClass(), propertyNameOperand.getVariable(), fragment);
                        Function<Object, Object> function = propertyReadAccelerators.get(field);
                        if (function != null)
                        {
                            propertyGetter = function;
                        }
                        else
                        {
                            ValueAccessor valueAccessor =  new ValueAccessor(field);
                            propertyGetter = v -> valueAccessor.get(v);
                        }
                        return propertyGetter.apply(instance);
                    }
                }
            }
            return propertyGetter.apply(typeOperand.calculate(contextParam));
        }
    }
}
