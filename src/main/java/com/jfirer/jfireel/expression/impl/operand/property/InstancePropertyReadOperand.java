package com.jfirer.jfireel.expression.impl.operand.property;

import com.jfirer.baseutil.reflect.valueaccessor.ValueAccessor;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

public class InstancePropertyReadOperand extends PropertyReadOperand
{
    private volatile boolean                  init = false;
    private          Function<Object, Object> propertyGetter;
    private          ValueAccessor            valueAccessor;

    public InstancePropertyReadOperand(Operand typeOperand, VariableOperand propertyNameOperand, String fragment, Map<Field, Function<Object, Object>> propertyReadAccelerators)
    {
        super(typeOperand, propertyNameOperand, fragment, propertyReadAccelerators);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        if (init == false)
        {
            synchronized (this)
            {
                if (init == false)
                {
                    init = true;
                    Object                   instance = typeOperand.calculate(contextParam);
                    Field                    field    = findField(instance.getClass(), propertyNameOperand.getVariable(), fragment);
                    Function<Object, Object> function = propertyReadAccelerators.get(field);
                    if (function != null)
                    {
                        propertyGetter = function;
                        return function.apply(instance);
                    }
                    else
                    {
                        valueAccessor = ValueAccessor.compile(field);
                        return valueAccessor.get(instance);
                    }
                }
            }
        }
        return valueAccessor != null ? valueAccessor.get(typeOperand.calculate(contextParam)) : propertyGetter.apply(typeOperand.calculate(contextParam));
    }
}
