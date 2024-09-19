package com.jfirer.jfireel.expression.impl.operand.method;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.jfireel.expression.Operand;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class StaticMethod extends MethodInvokeOperand
{
    private Class ckazz;

    public StaticMethod(Class ckass, String methodName, Operand[] methodParams, String fragment, Map<Method, MethodInvokeHelper> methodInvokeAccelerators)
    {
        super(methodName, methodParams, fragment, methodInvokeAccelerators);
        this.ckazz = ckass;
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
                    Object[] methodParamValues = Arrays.stream(methodParams).map(operand -> operand.calculate(contextParam)).toArray(Object[]::new);
                    Method   executable        = (Method) MethodInvokeHelper.findExecutable(Stream.iterate(ckazz, c -> c != Object.class, Class::getSuperclass).flatMap(c -> Arrays.stream(c.getDeclaredMethods())).toList(), methodParamValues, memberName);
                    if (executable == null)
                    {
                        throw new IllegalArgumentException("解析过程中发现未能发现匹配的方法,方法名为:" + memberName + "。异常解析位置为" + fragment);
                    }
                    final int[] classIds = Arrays.stream(executable.getParameterTypes()).mapToInt(ReflectUtil::getClassId).toArray();
                    invokeHelper   = methodInvokeAccelerators.getOrDefault(executable, (obj, argOperands, context) -> {
                        Object[] _args = new Object[argOperands.length];
                        for (int i = 0; i < _args.length; i++)
                        {
                            _args[i] = argOperands[i].calculate(context);
                        }
                        try
                        {
                            return executable.invoke(null, MethodInvokeHelper.compatibleValues(_args, classIds));
                        }
                        catch (IllegalAccessException | InvocationTargetException e)
                        {
                            throw new RuntimeException(e);
                        }
                    });
                    methodIdentify = true;
                    return executable.invoke(null, MethodInvokeHelper.compatibleValues(methodParamValues, classIds));
                }
            }
        }
        return invokeHelper.invoke(null, methodParams, contextParam);
    }
}
