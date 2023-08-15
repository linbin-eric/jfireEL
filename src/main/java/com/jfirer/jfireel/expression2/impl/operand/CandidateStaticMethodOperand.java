package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Data
public class CandidateStaticMethodOperand implements Operand
{
    private final List<Method> methods;

    public CandidateStaticMethodOperand(List<Method> methods)
    {
        this.methods = methods;
    }

    @Override
    public Object calculate(Map<String, Object> param)
    {
        throw new UnsupportedOperationException();
    }
}
