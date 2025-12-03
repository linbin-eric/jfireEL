package cc.jfire.el.expression.impl.operand;

import cc.jfire.el.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class BooleanOperand implements Operand
{
    private final boolean value;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return value;
    }
}
