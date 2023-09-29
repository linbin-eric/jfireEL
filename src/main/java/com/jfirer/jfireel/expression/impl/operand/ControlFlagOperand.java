package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.ControlFlag;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.ProcessControlResult;
import lombok.Data;

import java.util.Map;

@Data
public class ControlFlagOperand implements Operand
{
    public static final ControlFlagOperand   RETURN_OPERAND   = new ControlFlagOperand(new ProcessControlResult(ControlFlag.RETURN, null));
    public static final ControlFlagOperand   BREAK_OPERAND    = new ControlFlagOperand(new ProcessControlResult(ControlFlag.BREAK, null));
    public static final ControlFlagOperand   CONTINUE_OPERAND = new ControlFlagOperand(new ProcessControlResult(ControlFlag.CONTINUE, null));
    private final       ProcessControlResult result;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return result;
    }
}
