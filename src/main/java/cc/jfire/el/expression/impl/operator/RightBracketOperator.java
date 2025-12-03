package cc.jfire.el.expression.impl.operator;

import cc.jfire.el.PlaceHolder;

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
