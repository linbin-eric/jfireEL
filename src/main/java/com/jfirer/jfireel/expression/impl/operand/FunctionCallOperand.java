package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class FunctionCallOperand implements Operand
{
    private String functionName;
    private String[] paramNames;
    private Operand  function;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Map<String, Object> functionParam = new HashMap<>();
        for (String paramName : paramNames)
        {
            functionParam.put(paramName, contextParam.get(paramName));
        }
        return function.calculate(functionParam);
    }

    public static class   FunctionCallData{

    }
}
