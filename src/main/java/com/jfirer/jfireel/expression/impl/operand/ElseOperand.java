package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class ElseOperand implements Operand
{
    private MethodStructureOperand body;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearFragment()
    {
        body.clearFragment();
    }
}
