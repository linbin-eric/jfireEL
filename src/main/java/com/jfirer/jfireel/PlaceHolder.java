package com.jfirer.jfireel;

import com.jfirer.jfireel.expression.Operand;

import java.util.Map;

public interface PlaceHolder extends Operand
{
    default Object calculate(Map<String, Object> contextParam)
    {
        throw new IllegalArgumentException("占位符，不应该执行计算");
    }

    /**
     * 代表左侧的'['占位符
     */
    PlaceHolder LeftBracketPlaceHolder = new PlaceHolder()
    {
    };
    /**
     * 代表左侧的'{'占位符
     */
    PlaceHolder LeftAngleBracket       = new PlaceHolder()
    {
    };
}
