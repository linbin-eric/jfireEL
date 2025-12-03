package cc.jfire.el.expression.impl.operand.basic;

import cc.jfire.el.expression.Operand;
import lombok.Getter;

public abstract class BasicOperandImpl implements Operand
{
    protected String  operator;
    protected Operand left;
    protected Operand right;
    @Getter
    protected String  fragment;

    public BasicOperandImpl(String operator, Operand left, Operand right, String fragment)
    {
        this.operator = operator;
        this.left     = left;
        this.right    = right;
        this.fragment = fragment;
    }

    @Override
    public void clearFragment()
    {
        fragment = null;
        left.clearFragment();
        right.clearFragment();
    }
}
