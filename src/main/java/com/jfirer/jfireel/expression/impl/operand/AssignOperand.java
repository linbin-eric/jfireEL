package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class AssignOperand implements Operand
{
    protected final String  variableName;
    protected final Operand value;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        if (contextParam.containsKey(variableName) == false)
        {
            throw new IllegalArgumentException("变量名:" + variableName + "不存在上下文中，请检查");
        }
        contextParam.put(variableName, value.calculate(contextParam));
        return null;
    }

    @Override
    public void clearFragment()
    {
        value.clearFragment();
    }

    public static class CreateVariableAndAssignOperand extends AssignOperand
    {
        public CreateVariableAndAssignOperand(String name, Operand value)
        {
            super(name, value);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            contextParam.put(variableName, value.calculate(contextParam));
            return null;
        }
    }
}
