package com.jfirer.jfireel.expression.impl.operand.basic;

import com.jfirer.jfireel.expression.Operand;
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
