package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;

@Data
public abstract class PropertyReadOperand implements Operand
{
    protected final    Operand         typeOperand;
    protected final    VariableOperand propertyNameOperand;
    protected final    String          fragment;
    protected volatile Field           field;

    protected Field findField(Class<?> ckass, String fieldName, String fragment)
    {
        while (ckass != Object.class)
        {
            try
            {
                Field candidate = ckass.getDeclaredField(fieldName);
                if (candidate == null)
                {
                    ckass = ckass.getSuperclass();
                }
                else
                {
                    candidate.setAccessible(true);
                    return candidate;
                }
            }
            catch (NoSuchFieldException e)
            {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalArgumentException("解析属性，未能发现属性，异常解析表达式位置为：" + fragment);
    }

    public static class StaticClassPropertyOperand extends PropertyReadOperand
    {
        public StaticClassPropertyOperand(Operand typeOperand, VariableOperand propertyNameOperand, String fragment)
        {
            super(typeOperand, propertyNameOperand, fragment);
            Class<?> ckass = ((StaticClassOperand) typeOperand).getStaticClass();
            field = findField(ckass, propertyNameOperand.getVariable(), fragment);

        }

        @Override
        public Object calculate(Map<String, Object> param)
        {
            try
            {
                return field.get(null);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public static class InstancePropertyReadOperand extends PropertyReadOperand
    {
        public InstancePropertyReadOperand(Operand typeOperand, VariableOperand propertyNameOperand, String fragment)
        {
            super(typeOperand, propertyNameOperand, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> param)
        {
            if (field == null)
            {
                synchronized (this)
                {
                    if (field == null)
                    {
                        Object instance = typeOperand.calculate(param);
                        field = findField(instance.getClass(), propertyNameOperand.getVariable(), fragment);
                        field.setAccessible(true);
                        try
                        {
                            return field.get(instance);
                        }
                        catch (IllegalAccessException e)
                        {
                            ReflectUtil.throwException(e);
                            return null;
                        }
                    }
                }
            }
            try
            {
                return field.get(typeOperand.calculate(param));
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
