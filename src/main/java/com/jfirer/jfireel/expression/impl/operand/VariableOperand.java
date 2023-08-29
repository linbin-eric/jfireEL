package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class VariableOperand implements Operand
{
    private final String variable;

    public VariableOperand(String variable)
    {
        this.variable = variable;
    }

    @Override
    public Object calculate(Map<String, Object> param)
    {
        return param.get(variable);
    }
}
