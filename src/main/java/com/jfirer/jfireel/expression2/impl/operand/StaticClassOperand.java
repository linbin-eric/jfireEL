package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class StaticClassOperand implements Operand
{
    private final Class<?> ckass;

    public StaticClassOperand(Class<?> ckass) {this.ckass = ckass;}

    @Override
    public Object calculate(Map<String, Object> param)
    {
        return ckass;
    }
}
