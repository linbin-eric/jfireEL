package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.PlaceHolder;

public class RightParenOperator extends AbstractRightOperator
{
    private final String fragment;

    public RightParenOperator(String fragment)
    {
        super(LeftParenOperator.class, PlaceHolder.LEFT_PAREN);
        this.fragment = fragment;
    }

    @Override
    public int priority()
    {
        return 0;
    }
}
