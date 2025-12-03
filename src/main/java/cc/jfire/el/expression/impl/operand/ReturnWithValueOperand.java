package cc.jfire.el.expression.impl.operand;

import cc.jfire.el.expression.ControlFlag;
import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.ProcessControlResult;
import lombok.Data;

import java.util.Map;

@Data
public class ReturnWithValueOperand implements Operand
{
    private final Operand valueOperand;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Object calculate = valueOperand.calculate(contextParam);
        return new ProcessControlResult(ControlFlag.RETURN_WITH_VALUE, calculate);
    }

    @Override
    public void clearFragment()
    {
        valueOperand.clearFragment();
    }
}
