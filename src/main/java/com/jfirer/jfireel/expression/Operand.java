package com.jfirer.jfireel.expression;

import com.jfirer.baseutil.reflect.ValueAccessor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface Operand
{
    ConcurrentHashMap<Class, Consumer<Object>> translator = new ConcurrentHashMap<>();

    Object calculate(Map<String, Object> contextParam);

    default <T extends HashMap<String, Object>> Object calculate(T contextParam)
    {
        if (contextParam == null)
        {
            return calculate();
        }
        else if (!contextParam.isEmpty())
        {
            return calculate((Map<String, Object>) contextParam);
        }
        else
        {
            Class<?> ckass = contextParam.getClass();
            Consumer<Object> computed = translator.computeIfAbsent(ckass, type -> {
                ValueAccessor[] array = Stream.iterate(type, t -> t != HashMap.class, t -> t.getSuperclass()).flatMap(t -> Arrays.stream(t.getDeclaredFields()).map(field -> new ValueAccessor(field))).toArray(ValueAccessor[]::new);
                return (a) -> {
                    for (ValueAccessor valueAccessor : array)
                    {
                        ((Map<String, Object>) a).put(valueAccessor.getField().getName(), valueAccessor.get(a));
                    }
                };
            });
            computed.accept(contextParam);
            return calculate((Map<String, Object>) contextParam);
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
