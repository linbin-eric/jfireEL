package com.jfirer.jfireel.expression.impl.operand.method;

import com.jfirer.jfireel.expression.Matrix;
import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

@Data
public abstract class MethodInvokeOperand implements Operand
{
    protected final    String        memberName;
    protected final    Operand[]     argOperands;
    protected          String        fragment;
    protected volatile boolean       init = false;
    protected          MethodInvoker invoker;
    protected          Matrix        matrix;

    public MethodInvokeOperand(String memberName, Operand[] argOperands, String fragment, Matrix matrix)
    {
        this.memberName  = memberName;
        this.argOperands = argOperands;
        this.fragment    = fragment;
        this.matrix      = matrix;
    }

    @Override
    public void clearFragment()
    {
        fragment = null;
        if (argOperands != null)
        {
            for (Operand each : argOperands)
            {
                each.clearFragment();
            }
        }
    }
}
