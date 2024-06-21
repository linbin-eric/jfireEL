package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;

import java.util.Map;

/**
 * 作为边界操作数来使用，用于在解析的过程中确定左右括号内的范围。没有实际的计算。
 */
public class LeftParenOperand implements Operand
{
    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        throw new UnsupportedOperationException();
    }
}
