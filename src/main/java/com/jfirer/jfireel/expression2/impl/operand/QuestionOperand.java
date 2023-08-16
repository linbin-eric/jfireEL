package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class QuestionOperand implements Operand
{
    private final Operand condition;
    private final Operand left;
    private final Operand right;

    @Override
    public Object calculate(Map<String, Object> param)
    {
        Object calculate = condition.calculate(param);
        if ((Boolean) calculate)
        {
            return left.calculate(param);
        }
        else
        {
            return right.calculate(param);
        }
    }
}
