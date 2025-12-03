package cc.jfire.el.expression.impl.operand;

import cc.jfire.el.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class VariableOperand implements Operand
{
    private final String variable;

    public VariableOperand(String variable)
    {
        this.variable = variable;
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Object value = contextParam.get(variable);
        if (value == null)
        {
            if (contextParam.containsKey(variable))
            {
                return null;
            }
            else
            {
                throw new IllegalArgumentException("变量名:" + variable + "不存在上下文中，请检查");
            }
        }
        return value;
    }
}
