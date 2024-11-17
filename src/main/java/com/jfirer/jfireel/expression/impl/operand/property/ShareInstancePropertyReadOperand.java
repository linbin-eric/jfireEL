package com.jfirer.jfireel.expression.impl.operand.property;

import com.jfirer.baseutil.reflect.valueaccessor.ValueAccessor;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

@Data
public class ShareInstancePropertyReadOperand implements Operand
{
    protected final  Operand                              typeOperand;
    protected final  VariableOperand                      propertyNameOperand;
    protected        String                               fragment;
    protected final  Map<Field, Function<Object, Object>> propertyReadAccelerators;
    private volatile boolean                              init = false;
    private          Function<Object, Object>             propertyGetter;

    public ShareInstancePropertyReadOperand(Operand typeOperand, VariableOperand propertyNameOperand, String fragment, Map<Field, Function<Object, Object>> propertyReadAccelerators)
    {
        this.typeOperand              = typeOperand;
        this.propertyNameOperand      = propertyNameOperand;
        this.fragment                 = fragment;
        this.propertyReadAccelerators = propertyReadAccelerators;
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
                    Object instance = typeOperand.calculate(contextParam);
                    if (instance == null)
                    {
                        throw new NullPointerException("需要解析属性，但是对象为空，解析片段为:" + fragment);
                    }
                    Field field = Operand.findField(instance.getClass(), propertyNameOperand.getVariable(), fragment);
                    Function<Object, Object> function = propertyReadAccelerators.computeIfAbsent(field, f -> {
                        ValueAccessor valueAccessor = ValueAccessor.standard(f);
                        return o -> valueAccessor.get(o);
                    });
                    propertyGetter = function;
                    init           = true;
                    return function.apply(instance);
                }
            }
        }
        Object instance = typeOperand.calculate(contextParam);
        if (instance == null)
        {
            throw new NullPointerException("需要读取属性" + propertyNameOperand.getVariable() + "但是对象为空");
        }
        return propertyGetter.apply(instance);
    }

    @Override
    public void clearFragment()
    {
        fragment = null;
        typeOperand.clearFragment();
        propertyNameOperand.clearFragment();
    }
}
