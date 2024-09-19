package com.jfirer.jfireel.expression.impl.operand.method;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.jfireel.expression.Operand;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConstructorMethod extends MethodInvokeOperand
{
    private Class ckass;

    public ConstructorMethod(Class<?> ckass, Operand[] methodParams, String fragment, Map<Method, MethodInvokeHelper> methodInvokeAccelerators)
    {
        super(ckass.getName(), methodParams, fragment, methodInvokeAccelerators);
        this.ckass = ckass;
    }

    @SneakyThrows
    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        if (!methodIdentify)
        {
            synchronized (this)
            {
                if (!methodIdentify)
                {
                    Object[]    args       = Arrays.stream(methodParams).map(operand -> operand.calculate(contextParam)).toArray(Object[]::new);
                    Executable  executable = MethodInvokeHelper.findExecutable(List.of(ckass.getConstructors()), args, memberName);
                    final int[] classIds   = Arrays.stream(executable.getParameterTypes()).mapToInt(ReflectUtil::getClassId).toArray();
                    if (executable == null)
                    {
                        throw new IllegalArgumentException("解析过程中发现未能发现匹配的构造方法。异常解析位置为" + fragment);
                    }
                    Constructor constructor = (Constructor) executable;
                    invokeHelper   = (obj, argOperands, context) -> {
                        Object[] _args = new Object[argOperands.length];
                        for (int i = 0; i < _args.length; i++)
                        {
                            _args[i] = argOperands[i].calculate(contextParam);
                        }
                        try
                        {
                            return constructor.newInstance(MethodInvokeHelper.compatibleValues(_args, classIds));
                        }
                        catch (IllegalAccessException | InvocationTargetException | InstantiationException e)
                        {
                            throw new RuntimeException(e);
                        }
                    };
                    methodIdentify = true;
                    return ((Constructor<?>) executable).newInstance(MethodInvokeHelper.compatibleValues(args, classIds));
                }
            }
        }
        return invokeHelper.invoke(null, methodParams, contextParam);
    }
}
