package com.jfirer.jfireel.expression;

import java.util.HashMap;
import java.util.Map;

public interface Operand
{
    Object calculate(Map<String, Object> param);

    ThreadLocal<Map<String, Object>> DEFAULT = ThreadLocal.withInitial(HashMap::new);

    default Object calculate()
    {
        Map<String, Object> map    = DEFAULT.get();
        Object              result = calculate(map);
        map.clear();
        return result;
    }
}
