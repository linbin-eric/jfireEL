package cc.jfire.el;

import cc.jfire.el.expression.Operand;

import java.util.Map;

public interface PlaceHolder extends Operand
{
    default Object calculate(Map<String, Object> contextParam)
    {
        throw new IllegalArgumentException("占位符，不应该执行计算");
    }

    /**
     * 代表左侧的'['占位符,方括号
     */
    PlaceHolder LEFT_BRACKET = new PlaceHolder()
    {
    };
    /**
     * 代表左侧的'{'占位符，大括号
     */
    PlaceHolder LEFT_BRACE   = new PlaceHolder()
    {
    };
    /**
     * 代表左侧的'('占位符，圆括号
     */
    PlaceHolder LEFT_PAREN   = new PlaceHolder()
    {
    };
    /**
     * 代表左侧的&lt;%，
     */
    PlaceHolder SET_START    = new PlaceHolder()
    {
    };
}
