package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class ElseIfOperand implements Operand
{
    private Operand condition;
    private Operand body;

    @Override
    public Object calculate(Map<String, Object> param)
    {
        throw new UnsupportedOperationException();
    }
}
