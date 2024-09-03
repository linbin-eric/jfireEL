package com.jfirer.jfireel.expression;

import com.jfirer.baseutil.reflect.ValueAccessor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public interface Operand
{
    class InnerContextParam
    {
        private final Map<String, Object> map   = new HashMap<>();
        private       boolean             inUse = false;
    }

    ThreadLocal<InnerContextParam>                                    INNER_CONTEXT_PARAM = ThreadLocal.withInitial(InnerContextParam::new);
    ConcurrentHashMap<Class, BiConsumer<Object, Map<String, Object>>> translator          = new ConcurrentHashMap<>();

    Object calculate(Map<String, Object> contextParam);

    default Object calculate(Object objParam)
    {
        if (objParam == null)
        {
            return calculate();
        }
        else
        {
            Class<?> ckass = objParam.getClass();
            BiConsumer<Object, Map<String, Object>> computed = translator.computeIfAbsent(ckass, type -> {
                ValueAccessor[] array = Stream.iterate(type, t -> t != HashMap.class && t != Object.class, t -> t.getSuperclass()).flatMap(t -> Arrays.stream(t.getDeclaredFields()).map(field -> new ValueAccessor(field))).toArray(ValueAccessor[]::new);
                return (obj, map) -> {
                    for (ValueAccessor valueAccessor : array)
                    {
                        map.put(valueAccessor.getField().getName(), valueAccessor.get(obj));
                    }
                };
            });
            InnerContextParam innerContextParam = INNER_CONTEXT_PARAM.get();
            if (innerContextParam.inUse == false)
            {
                innerContextParam.inUse = true;
                computed.accept(objParam, innerContextParam.map);
                Object result = calculate(innerContextParam.map);
                innerContextParam.inUse = false;
                innerContextParam.map.clear();
                return result;
            }
            else
            {
                computed.accept(objParam, innerContextParam.map);
                return calculate(innerContextParam.map);
            }
        }
    }

    default Object calculate()
    {
        InnerContextParam innerContextParam = INNER_CONTEXT_PARAM.get();
        if (innerContextParam.inUse == false)
        {
            innerContextParam.inUse = true;
            Object result = calculate(innerContextParam.map);
            innerContextParam.inUse = false;
            innerContextParam.map.clear();
            return result;
        }
        else
        {
            return calculate(innerContextParam.map);
        }
    }
}
