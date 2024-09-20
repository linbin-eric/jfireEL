package com.jfirer.jfireel.expression.impl.operand.method;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.lang.reflect.Executable;
import java.util.Map;

@Data
public abstract class MethodInvokeOperand implements Operand
{
    protected final    String                         memberName;
    protected final    Operand[]                      argOperands;
    protected final    String                         fragment;
    protected final    Map<Executable, MethodInvoker> methodInvokeAccelerators;
    protected volatile boolean                        init = false;
    protected          MethodInvoker                  invoker;
}
