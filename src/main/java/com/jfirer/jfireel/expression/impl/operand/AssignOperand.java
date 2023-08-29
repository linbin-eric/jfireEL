package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class AssignOperand implements Operand
{
    protected final String  name;
    protected final Operand value;

    @Override
    public Object calculate(Map<String, Object> param)
    {
        if (param.containsKey(name)==false)
        {
            throw new IllegalArgumentException("变量名:" + name + "不存在上下文中，请检查");
        }
        param.put(name, value.calculate(param));
        return null;
    }

    public static class CreateVariableAndAssignOperand extends AssignOperand
    {
        public CreateVariableAndAssignOperand(String name, Operand value)
        {
            super(name, value);
        }

        @Override
        public Object calculate(Map<String, Object> param)
        {
            param.put(name, value.calculate(param));
            return null;
        }
    }
}
