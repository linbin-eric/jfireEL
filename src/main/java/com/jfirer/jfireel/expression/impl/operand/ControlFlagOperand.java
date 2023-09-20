package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.ControlFlag;
import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Map;

public abstract class ControlFlagOperand implements Operand
{
    public static final ReturnOperand   RETURN_OPERAND   = new ReturnOperand();
    public static final BreakOperand    BREAK_OPERAND    = new BreakOperand();
    public static final ContinueOperand CONTINUE_OPERAND = new ContinueOperand();

    static class ReturnOperand extends ControlFlagOperand
    {
        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            return ControlFlag.RETURN;
        }
    }

    static class BreakOperand extends ControlFlagOperand
    {
        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            return ControlFlag.BREAK;
        }
    }

    static class ContinueOperand extends ControlFlagOperand
    {
        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            return ControlFlag.CONTINUE;
        }
    }

    @Data
    public static class ReturnWithResultOperand extends ControlFlagOperand
    {
        private final Operand value;

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            return new ReturnWithValue(value.calculate(contextParam));
        }
    }

    record ReturnWithValue(Object value)
    {
    }
}
