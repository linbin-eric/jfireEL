package cc.jfire.el.expression.impl.operand;

import cc.jfire.el.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class LiteralOparand implements Operand
{
    private final String literal;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return literal;
    }
}
