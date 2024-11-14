package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Map;
import java.util.function.BiFunction;

@Data
public class InnerCallOperand implements Operand
{
    private final BiFunction<Map<String, Object>, Operand[], Object> function;
    private       Operand[]                                          methodParams;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return function.apply(contextParam, methodParams);
    }

    @Override
    public void clearFragment()
    {
        for (Operand methodParam : methodParams)
        {
            methodParam.clearFragment();
        }
    }
}
