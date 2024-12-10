package com.jfirer.jfireel.expression.impl.operand.property;

import com.jfirer.baseutil.reflect.valueaccessor.ValueAccessor;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.Matrix;
import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

@Data
public class InstancePropertyReadOperand implements Operand
{
    protected final  Operand                  instanceOperand;
    protected final  String                   propertyName;
    protected final  Matrix                   matrix;
    protected        String                   fragment;
    private volatile boolean                  init = false;
    private          Function<Object, Object> propertyGetter;
    private          ValueAccessor            valueAccessor;

    public InstancePropertyReadOperand(Operand instanceOperand, String propertyName, String fragment, Matrix matrix)
    {
        this.instanceOperand = instanceOperand;
        this.propertyName    = propertyName;
        this.fragment        = fragment;
        this.matrix          = matrix;
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
                    Object instance = instanceOperand.calculate(contextParam);
                    if (instance == null)
                    {
                        throw new NullPointerException("需要解析属性，但是对象为空，解析片段为:" + fragment);
                    }
                    Field                    field    = Operand.findField(instance.getClass(), propertyName, fragment);
                    Function<Object, Object> function = matrix.findAcceleratorForPropertyRead(field);
                    if (function != null)
                    {
                        propertyGetter = function;
                        init           = true;
                        return function.apply(instance);
                    }
                    else
                    {
                        valueAccessor = Expression.SHARE_VALUEACCESSOR_CACHE.computeIfAbsent(field, ValueAccessor::standard);
                        init          = true;
                        return valueAccessor.get(instance);
                    }
                }
            }
        }
        Object instance = instanceOperand.calculate(contextParam);
        if (instance == null)
        {
            throw new NullPointerException("需要读取属性" + propertyName + "但是对象为空");
        }
        return valueAccessor != null ? valueAccessor.get(instance) : propertyGetter.apply(instance);
    }

    @Override
    public void clearFragment()
    {
        fragment = null;
        instanceOperand.clearFragment();
    }
}
