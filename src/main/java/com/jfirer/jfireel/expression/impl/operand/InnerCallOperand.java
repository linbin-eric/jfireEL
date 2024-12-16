package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.method.MethodInvoker;
import lombok.Data;

import java.util.Map;

/**
 * 内部调用方法，也就是为接口MethodInvoker注册一个内部名称，使得可以在EL表达式中使用。
 */
@Data
public class InnerCallOperand extends CallOperand
{
    private final MethodInvoker function;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return function.invoke(null, args, contextParam);
    }

    @Override
    public void clearFragment()
    {
        if (args != null)
        {
            for (Operand methodParam : args)
            {
                methodParam.clearFragment();
            }
        }
    }
}
