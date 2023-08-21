package com.jfirer.jfireel.expression2;

public interface Operator
{
    int priority();

    void push(ParseContext parseContext);

    void onPop(ParseContext parseContext);

    default boolean isBoundary()
    {
        return false;
    }
}
