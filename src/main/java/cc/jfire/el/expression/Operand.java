package cc.jfire.el.expression;


import cc.jfire.baseutil.smc.compiler.CompileHelper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface Operand
{
    CompileHelper COMPILE_HELPER = new CompileHelper();
    AtomicInteger COUNTER        = new AtomicInteger(1);

    Object calculate(Map<String, Object> contextParam);

    default Object calculate()
    {
        return calculate(new HashMap<>());
    }

    default void clearFragment()
    {
    }

    static Field findField(Class<?> ckass, String fieldName, String fragment)
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
}
