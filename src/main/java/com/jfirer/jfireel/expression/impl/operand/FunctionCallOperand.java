package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class FunctionCallOperand extends CallOperand
{
    private final String[] paramNames;
    private final Operand  function;
    private final boolean  supportVariableParams;
    private       String   functionName;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Map<String, Object> functionParam = new HashMap<>();
        if (supportVariableParams)
        {
            int len = paramNames.length - 1;
            for (int i = 0; i < len; i++)
            {
                functionParam.put(paramNames[i], args[i].calculate(contextParam));
            }
            Object[] array = new Object[args.length - len];
            for (int i = len; i < args.length; i++)
            {
                array[i - len] = args[i].calculate(contextParam);
            }
            functionParam.put(paramNames[len], array);
        }
        else
        {
            for (int i = 0; i < paramNames.length; i++)
            {
                functionParam.put(paramNames[i], args[i].calculate(contextParam));
            }
        }
        return function.calculate(functionParam);
    }

    @Override
    public Object calculate(Object[] args)
    {
        Map<String, Object> functionParam = new HashMap<>();
        if (supportVariableParams)
        {
            int len = paramNames.length - 1;
            for (int i = 0; i < len; i++)
            {
                functionParam.put(paramNames[i], args[i]);
            }
            Object[] array = new Object[args.length - len];
            for (int i = len; i < args.length; i++)
            {
                array[i - len] = args[i];
            }
            functionParam.put(paramNames[len], array);
        }
        else
        {
            for (int i = 0; i < paramNames.length; i++)
            {
                functionParam.put(paramNames[i], args[i]);
            }
        }
        return function.calculate(functionParam);
    }
}
