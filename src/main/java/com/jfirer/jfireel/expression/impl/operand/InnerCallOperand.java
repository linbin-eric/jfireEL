package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.method.MethodInvoker;
import lombok.Data;

import java.util.Map;

@Data
public class InnerCallOperand implements Operand
{
    private final MethodInvoker function;
    private       Operand[]     methodParams;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return function.invoke(null, methodParams, contextParam);
    }

    @Override
    public void clearFragment()
    {
        if (methodParams != null)
        {
            for (Operand methodParam : methodParams)
            {
                methodParam.clearFragment();
            }
        }
    }
}
