package com.jfireframework.jfireel;

import java.util.HashMap;
import java.util.Map;

public enum Operator implements CalculateType
{
    PLUS("+"), //
    SUB("-"), //
    MULTI("*"), //
    DIVISION("/"), //
    QUESTION("?"), //
    EQ("=="), //
    GT(">"), //
    LT("<"), //
    PERCENT("%"), //
    COLON(":"), //
    LT_EQ("<="), //
    GT_EQ(">="), //
    BANG_EQ("!="), //
    DOUBLE_AMP("&&"), //
    DOUBLE_BAR("||"), //
    ;
    private static Map<String, Operator> symbols = new HashMap<String, Operator>(128);
    
    static
    {
        for (Operator each : Operator.values())
        {
            symbols.put(each.getLiterals(), each);
        }
    }
    
    private Operator(String literals)
    {
        this.literals = literals;
    }
    
    private final String literals;
    
    /**
     * 通过字面量查找词法符号.
     * 
     * @param literals 字面量
     * @return 词法符号
     */
    public static Operator literalsOf(final String literals)
    {
        return symbols.get(literals);
    }
    
    private String getLiterals()
    {
        return literals;
    }
}
