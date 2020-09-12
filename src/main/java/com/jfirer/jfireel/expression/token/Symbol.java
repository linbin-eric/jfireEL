package com.jfirer.jfireel.expression.token;

import java.util.HashMap;
import java.util.Map;

public enum Symbol implements Token
{
    LEFT_PAREN("("), //
    RIGHT_PAREN(")"), //
    LEFT_BRACKET("["), //
    RIGHT_BRACKET("]"), //
    COMMA(",")//
    ;

    private static final Map<String, Symbol> symbols = new HashMap<String, Symbol>(128);

    static
    {
        for (Symbol each : symbols.values())
        {
            symbols.put(each.getLiterals(), each);
        }
    }

    private final String literals;

    Symbol(String literals)
    {
        this.literals = literals;
    }

    /**
     * 通过字面量查找词法符号.
     *
     * @param literals 字面量
     * @return 词法符号
     */
    public static Symbol literalsOf(final String literals)
    {
        return symbols.get(literals);
    }

    public String getLiterals()
    {
        return literals;
    }
}
