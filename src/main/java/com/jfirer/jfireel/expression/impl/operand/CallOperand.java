package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Data
@Accessors(chain = true)
public abstract class CallOperand implements Operand
{
    protected Operand[] args;

    @Data
    @Accessors(chain = true)
    public static class CallOperandData
    {
        String                           name;
        int                              paramCount;
        boolean                          supportVariableParams;
        Class[] parameterTypes;
        Function<Operand[], CallOperand> constructor;
    }

    public abstract Object calculate(Object[] args);
}
