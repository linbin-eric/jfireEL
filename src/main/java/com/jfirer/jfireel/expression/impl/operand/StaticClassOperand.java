package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class StaticClassOperand implements Operand
{
    private final Class<?> staticClass;

    public StaticClassOperand(Class<?> staticClass) {this.staticClass = staticClass;}

    @Override
    public Object calculate(Map<String, Object> param)
    {
        return staticClass;
    }
}
