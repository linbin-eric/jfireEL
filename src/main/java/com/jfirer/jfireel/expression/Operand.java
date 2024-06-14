package com.jfirer.jfireel.expression;

import com.jfirer.baseutil.reflect.ValueAccessor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public interface Operand
{
    static ConcurrentHashMap<Class, Function<Object, Map<String, Object>>> translator = new ConcurrentHashMap<>();

    Object calculate(Map<String, Object> contextParam);

    default Object calculate(Object contextParam)
    {
        if (contextParam == null)
        {
            return calculate();
        }
        else
        {
            Class<?> ckass = contextParam.getClass();
            Function<Object, Map<String, Object>> computed = translator.computeIfAbsent(ckass, type -> {
                Field[]         declaredFields = type.getDeclaredFields();
                ValueAccessor[] array          = Arrays.stream(declaredFields).map(field -> new ValueAccessor(field)).toArray(ValueAccessor[]::new);
                return (Object a) -> {
                    Map<String, Object> map = DEFAULT.get();
                    map.clear();
                    for (ValueAccessor valueAccessor : array)
                    {
                        map.put(valueAccessor.getField().getName(), valueAccessor.get(a));
                    }
                    return map;
                };
            });
            return calculate(computed.apply(contextParam));
        }
    }

    ThreadLocal<Map<String, Object>> DEFAULT = ThreadLocal.withInitial(HashMap::new);

    default Object calculate()
    {
        Map<String, Object> map    = DEFAULT.get();
        Object              result = calculate(map);
        map.clear();
        return result;
    }
}
