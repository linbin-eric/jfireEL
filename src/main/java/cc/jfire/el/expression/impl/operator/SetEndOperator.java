package cc.jfire.el.expression.impl.operator;

import cc.jfire.el.PlaceHolder;

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
