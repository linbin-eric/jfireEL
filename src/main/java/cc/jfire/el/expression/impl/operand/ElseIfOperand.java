package cc.jfire.el.expression.impl.operand;

import cc.jfire.el.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class ElseIfOperand implements Operand
{
    private Operand                condition;
    private MethodStructureOperand body;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearFragment()
    {
        condition.clearFragment();
    }
}
