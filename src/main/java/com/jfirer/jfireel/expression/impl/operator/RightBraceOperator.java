package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.PlaceHolder;

public class RightBraceOperator extends AbstractRightOperator
{
    private final String fragment;

    public RightBraceOperator(String fragment)
    {
        super(LeftBraceOperator.class, PlaceHolder.LEFT_BRACE);
        this.fragment = fragment;
    }

    @Override
    public int priority()
    {
        return 0;
    }
}
