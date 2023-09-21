package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.ControlFlag;
import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class MutliOperand implements Operand
{
    private final Operand[] operands;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Object calculate = null;
        for (Operand each : operands)
        {
            calculate = each.calculate(contextParam);
            if (calculate == ControlFlag.RETURN || calculate == ControlFlag.BREAK || calculate == ControlFlag.CONTINUE)
            {
                return calculate;
            }
            else if (calculate instanceof ControlFlagOperand.ReturnWithValue returnWithValue)
            {
                return calculate;
            }
        }
        return calculate;
    }
}
