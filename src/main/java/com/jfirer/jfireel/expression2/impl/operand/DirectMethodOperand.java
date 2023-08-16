package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Data
public class DirectMethodOperand implements Operand
{
    private final Method        method;
    private       List<Operand> methodParams;

    @Override
    public Object calculate(Map<String, Object> param)
    {
        throw new UnsupportedOperationException();
    }
}
