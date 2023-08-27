package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.util.Map;
import java.util.function.BiFunction;

@Data
public class InnerCallOperand implements Operand
{
    private final BiFunction<Map<String, Object>, Object[], Object> function;
    private       Operand[]                                         methodParams;

    @Override
    public Object calculate(Map<String, Object> param)
    {
        Object[] args = new Object[methodParams.length];
        for (int i = 0; i < args.length; i++)
        {
            args[i] = methodParams[i].calculate(param);
        }
        return function.apply(param, args);
    }
}
