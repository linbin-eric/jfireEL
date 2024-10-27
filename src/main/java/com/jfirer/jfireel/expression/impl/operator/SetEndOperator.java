package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.PlaceHolder;

public class SetEndOperator extends AbstractRightOperator
{
    private final String fragment;

    public SetEndOperator(String fragment)
    {
        super(SetStartOperator.class, PlaceHolder.SET_START);
        this.fragment = fragment;
    }

    @Override
    public int priority()
    {
        return 0;
    }
}
