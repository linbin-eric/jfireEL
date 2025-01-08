package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Matrix;
import com.jfirer.jfireel.expression.Operand;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.function.Function;

@Data
public abstract class CallOperand implements Operand
{
    protected Operand[] args;

    @Data
    @Accessors(chain = true)
    public static class CallOperandPlaceHolder implements Operand
    {
        private String    name;
        private Matrix    matrix;
        private Operand[] args;

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            throw new UnsupportedOperationException();
        }
    }

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
}
