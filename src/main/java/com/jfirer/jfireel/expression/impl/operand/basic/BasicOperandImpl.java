package com.jfirer.jfireel.expression.impl.operand.basic;

import com.jfirer.jfireel.expression.Operand;

import java.math.BigDecimal;
import java.util.Map;

public abstract class BasicOperandImpl implements Operand
{
    protected Operand left;
    protected Operand right;
    protected String  fragment;

    public BasicOperandImpl(Operand left, Operand right, String fragment)
    {
        this.left     = left;
        this.right    = right;
        this.fragment = fragment;
    }
}
