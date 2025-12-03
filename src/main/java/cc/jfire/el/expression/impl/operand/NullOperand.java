package cc.jfire.el.expression.impl.operand;

import cc.jfire.el.expression.Operand;

import java.util.Map;

public class NullOperand implements Operand
{
    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return null;
    }
}
