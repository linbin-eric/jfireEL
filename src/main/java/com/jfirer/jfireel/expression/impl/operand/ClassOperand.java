package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class ClassOperand implements Operand
{
    private final Class<?> ckass;

    public ClassOperand(Class<?> ckass) {this.ckass = ckass;}

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return ckass;
    }
}



