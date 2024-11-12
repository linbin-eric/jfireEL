package com.jfirer.jfireel.expression.impl.operand.method.standard;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.method.MethodInvokeOperand;
import com.jfirer.jfireel.expression.impl.operand.method.MethodInvoker;
import lombok.SneakyThrows;

import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class StaticMethod extends MethodInvokeOperand
{
    private Class ckazz;

    public StaticMethod(Class ckass, String methodName, Operand[] methodParams, String fragment, Map<Executable, MethodInvoker> methodInvokeAccelerators)
    {
        super(methodName, methodParams, fragment, methodInvokeAccelerators);
        this.ckazz = ckass;
    }

    @SneakyThrows
    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        if (!init)
        {
            synchronized (this)
            {
                if (!init)
                {
                    Object[] methodParamValues = Arrays.stream(argOperands).map(operand -> operand.calculate(contextParam)).toArray(Object[]::new);
                    Method   executable        = (Method) MethodInvoker.findExecutable(ckazz, methodParamValues, memberName);
                    if (executable == null)
                    {
                        throw new IllegalArgumentException("解析过程中发现未能发现匹配的方法,方法名为:" + memberName + "。异常解析位置为" + fragment);
                    }
                    executable.setAccessible(true);
                    final int[] classIds = Arrays.stream(executable.getParameterTypes()).mapToInt(ReflectUtil::getClassId).toArray();
                    invoker = methodInvokeAccelerators.getOrDefault(executable, (obj, argOperands, context) -> {
                        Object[] _args = new Object[argOperands.length];
                        for (int i = 0; i < _args.length; i++)
                        {
                            _args[i] = argOperands[i].calculate(context);
                        }
                        try
                        {
                            return executable.invoke(null, MethodInvoker.compatibleValues(_args, classIds));
                        }
                        catch (IllegalAccessException | InvocationTargetException e)
                        {
                            throw new RuntimeException(e);
                        }
                    });
                    init    = true;
                    return executable.invoke(null, MethodInvoker.compatibleValues(methodParamValues, classIds));
                }
            }
        }
        return invoker.invoke(null, argOperands, contextParam);
    }
}
