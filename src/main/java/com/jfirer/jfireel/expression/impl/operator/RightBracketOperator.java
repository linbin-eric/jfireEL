package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.PlaceHolder;

public class RightBracketOperator extends AbstractRightOperator
{
    private final String fragment;

    public RightBracketOperator(String fragment)
    {
        super(LeftBracketOperator.class, PlaceHolder.LEFT_BRACKET);
        this.fragment = fragment;
    }

    @Override
    public int priority()
    {
        return 0;
    }
}
