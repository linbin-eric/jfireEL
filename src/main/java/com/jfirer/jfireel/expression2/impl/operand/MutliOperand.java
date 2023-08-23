package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.ControlFlag;
import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class MutliOperand implements Operand
{
    private final Operand[] operands;

    @Override
    public Object calculate(Map<String, Object> param)
    {
        for (Operand each : operands)
        {
            Object calculate = each.calculate(param);
            if (calculate == ControlFlag.RETURN || calculate == ControlFlag.BREAK || calculate == ControlFlag.CONTINUE)
            {
                return calculate;
            }
        }
        return null;
    }
}
