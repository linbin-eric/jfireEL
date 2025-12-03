package cc.jfire.el.expression.impl.operand;

import cc.jfire.el.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class CreateVariableOperand implements Operand
{
    protected final String variableName;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        contextParam.put(variableName, null);
        return null;
    }
}
