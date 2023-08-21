package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class CreateVariableOperand implements Operand
{
    protected final String variableName;

    @Override
    public Object calculate(Map<String, Object> param)
    {
        param.put(variableName, null);
        return null;
    }
}
