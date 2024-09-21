package com.jfirer.jfireel.expression.impl.operand.basic;

import com.jfirer.jfireel.expression.Operand;

public abstract class BasicOperandImpl implements Operand
{
    protected String  operator;
    protected Operand left;
    protected Operand right;
    protected String  fragment;

    public BasicOperandImpl(String operator, Operand left, Operand right, String fragment)
    {
        this.operator = operator;
        this.left     = left;
        this.right    = right;
        this.fragment = fragment;
    }
}
