package com.jfirer.jfireel.expression.impl.operand.property;

import com.jfirer.baseutil.reflect.valueaccessor.ValueAccessor;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

@Data
public class InstancePropertyReadOperand implements Operand
{
    protected final  Operand                              typeOperand;
    protected final  VariableOperand                      propertyNameOperand;
    protected final  String                               fragment;
    protected final  Map<Field, Function<Object, Object>> propertyReadAccelerators;
    private volatile boolean                              init = false;
    private          Function<Object, Object>             propertyGetter;
    private          ValueAccessor                        valueAccessor;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        if (init == false)
        {
            synchronized (this)
            {
                if (init == false)
                {
                    Object instance = typeOperand.calculate(contextParam);
                    if (instance == null)
                    {
                        throw new NullPointerException("需要解析属性，但是对象为空，解析片段为:" + fragment);
                    }
                    Field                    field    = Operand.findField(instance.getClass(), propertyNameOperand.getVariable(), fragment);
                    Function<Object, Object> function = propertyReadAccelerators.get(field);
                    if (function != null)
                    {
                        propertyGetter = function;
                        init           = true;
                        return function.apply(instance);
                    }
                    else
                    {
                        valueAccessor = ValueAccessor.standard(field);
                        init          = true;
                        return valueAccessor.get(instance);
                    }
                }
            }
        }
        Object instance = typeOperand.calculate(contextParam);
        if (instance == null)
        {
            throw new NullPointerException("需要读取属性"+propertyNameOperand.getVariable()+"但是对象为空");
        }
        return valueAccessor != null ? valueAccessor.get(instance) : propertyGetter.apply(instance);

    }
}
