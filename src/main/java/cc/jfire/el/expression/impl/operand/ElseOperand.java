package cc.jfire.el.expression.impl.operand;

import cc.jfire.el.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class ElseOperand implements Operand
{
    private MethodStructureOperand body;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearFragment()
    {
        if (body != null)
        {
            body.clearFragment();
        }
    }
}
