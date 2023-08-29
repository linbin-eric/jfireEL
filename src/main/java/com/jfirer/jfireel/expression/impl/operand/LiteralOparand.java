package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class LiteralOparand implements Operand
{
    private final String literal;

    @Override
    public Object calculate(Map<String, Object> param)
    {
        return literal;
    }
}
