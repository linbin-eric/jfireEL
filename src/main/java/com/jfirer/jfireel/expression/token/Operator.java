package com.jfirer.jfireel.expression.token;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum Operator implements Token
{
    QUESTION("?", 0), //
    COLON(":", 0), //
    DOUBLE_AMP("&&", 1), //
    DOUBLE_BAR("||", 1), //
    EQ("==", 2), //
    GT(">", 2), //
    LT("<", 2), //
    LT_EQ("<=", 2), //
    GT_EQ(">=", 2), //
    NOT_EQ("!=", 2), //
    PLUS("+", 3), //
    MINUS("-", 3), //
    MULTI("*", 5), //
    DIVISION("/", 5), //
    PERCENT("%", 5), //
    ;
    private static final Map<String, Operator> symbols = new HashMap<String, Operator>(128);
    private static final Set<Operator>         store   = new HashSet<Operator>();

    static
    {
        for (Operator each : Operator.values())
        {
            symbols.put(each.getLiterals(), each);
            store.add(each);
        }
    }

    private final String literals;
    private final int    priority;

    Operator(String literals, int priority)
    {
        this.literals = literals;
        this.priority = priority;
    }

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

    public static boolean isOperator(Token token)
    {
        return token instanceof Operator;
    }

    public String getLiterals()
    {
        return literals;
    }

    public int getPriority()
    {
        return priority;
    }
}
