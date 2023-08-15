package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;

import java.util.Map;

public class OrOperand implements Operand
{
    private Operand left;
    private Operand right;
    private String  fragment;

    public OrOperand(Operand left, Operand right, String fragment)
    {
        this.left     = left;
        this.right    = right;
        this.fragment = fragment;
    }

    @Override
    public Object calculate(Map<String, Object> param)
    {
        Object leftValue  = left.calculate(param);
        Object rightValue = right.calculate(param);
        if (leftValue instanceof Boolean b1 && rightValue instanceof Boolean b2)
        {
            return b1.booleanValue() || b2.booleanValue();
        }
        else
        {
            throw new IllegalStateException("计算出现异常，&& 操作符左右不全是布尔变量，异常位置为" + fragment);
        }
    }
}
