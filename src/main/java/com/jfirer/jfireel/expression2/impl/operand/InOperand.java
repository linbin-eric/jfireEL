package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class InOperand implements Operand
{
    private final String  itemName;
    private final Operand itemsContainer;

    @Override
    public Object calculate(Map<String, Object> param)
    {
        throw new UnsupportedOperationException();
    }
}
