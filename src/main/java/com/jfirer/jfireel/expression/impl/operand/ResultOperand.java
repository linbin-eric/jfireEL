package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class ResultOperand implements Operand
{
    private final MutliOperand mutliOperand;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Object calculate = mutliOperand.calculate(contextParam);
        if (calculate instanceof ControlFlagOperand.ReturnWithValue returnWithValue)
        {
            return returnWithValue.value();
        }
        else
        {
            return calculate;
        }
    }
}
