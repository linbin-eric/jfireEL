package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class ElseOperand implements Operand
{
    private Operand body;

    @Override
    public Object calculate(Map<String, Object> param)
    {
        throw new UnsupportedOperationException();
    }
}
