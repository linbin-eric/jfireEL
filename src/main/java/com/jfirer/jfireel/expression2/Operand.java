package com.jfirer.jfireel.expression2;

import java.util.Map;

public interface Operand
{
    Object calculate(Map<String, Object> param);
}
