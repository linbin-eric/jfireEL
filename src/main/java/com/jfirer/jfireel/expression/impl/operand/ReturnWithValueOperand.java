package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.ControlFlag;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.ProcessControlResult;
import lombok.Data;

import java.util.Map;

@Data
public class ReturnWithValueOperand implements Operand
{
    private final Operand valueOperand;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Object calculate = valueOperand.calculate(contextParam);
        return new ProcessControlResult(ControlFlag.RETURN_WITH_VALUE, calculate);
    }

}
