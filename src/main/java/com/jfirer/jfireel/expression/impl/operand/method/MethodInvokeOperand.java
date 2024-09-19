package com.jfirer.jfireel.expression.impl.operand.method;

import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.lang.reflect.*;
import java.util.Map;

@Data
public abstract class MethodInvokeOperand implements Operand
{
    protected final      String                          memberName;
    protected final      Operand[]                       methodParams;
    protected final      String                          fragment;
    protected final      Map<Method, MethodInvokeHelper> methodInvokeAccelerators;
    protected volatile   boolean                         methodIdentify = false;
    protected            MethodInvokeHelper              invokeHelper;
}
