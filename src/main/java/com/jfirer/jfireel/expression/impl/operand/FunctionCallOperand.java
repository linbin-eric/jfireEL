package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class FunctionCallOperand implements Operand
{
    private final String[]  paramNames;
    private final Operand   function;
    private       String    functionName;
    private       Operand[] args;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Map<String, Object> functionParam = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++)
        {
            functionParam.put(paramNames[i], args[i].calculate(contextParam));
        }
        return function.calculate(functionParam);
    }

    @Data
    public static class FunctionCallData
    {
        private String   functionName;
        private String[] paramNames;
        private Operand  function;
    }
}
