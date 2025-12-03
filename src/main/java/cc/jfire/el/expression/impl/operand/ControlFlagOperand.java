package cc.jfire.el.expression.impl.operand;

import cc.jfire.el.expression.ControlFlag;
import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.ProcessControlResult;
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
