package com.jfirer.jfireel.expression.impl.operand.method;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.Operand;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class InstanceMethod extends MethodInvokeOperand
{
    private Operand instanceOperand;

    public InstanceMethod(Operand instanceOperand, String methodName, Operand[] methodParams, String fragment, Map<Method, MethodInvokeHelper> methodInvokeAccelerators)
    {
        super(methodName, methodParams, fragment, methodInvokeAccelerators);
        this.instanceOperand = instanceOperand;
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
                    Object instance = instanceOperand.calculate(contextParam);
                    if (instance == null)
                    {
                        throw new IllegalStateException("方法调用，但是调用对象为空，请检查是否变量名错误，异常位置为" + fragment);
                    }
                    Object[]    args       = Arrays.stream(methodParams).map(operand -> operand.calculate(contextParam)).toArray(Object[]::new);
                    Method      executable = (Method) MethodInvokeHelper.findExecutable(Stream.iterate((Class) instance.getClass(), c -> c != Object.class, Class::getSuperclass).flatMap(c -> Arrays.stream(c.getDeclaredMethods())).toList(), args, memberName);
                    final int[] classIds   = Arrays.stream(executable.getParameterTypes()).mapToInt(ReflectUtil::getClassId).toArray();
                    if (executable == null)
                    {
                        throw new IllegalArgumentException("解析过程中发现未能发现匹配的方法,方法名为:" + memberName + "。异常解析位置为" + fragment);
                    }
                    invokeHelper   = methodInvokeAccelerators.getOrDefault(executable, (obj, argOperands, context) -> {
                        Object[] _args = new Object[argOperands.length];
                        for (int i = 0; i < _args.length; i++)
                        {
                            _args[i] = argOperands[i].calculate(context);
                        }
                        try
                        {
                            return executable.invoke(obj, MethodInvokeHelper.compatibleValues(_args, classIds));
                        }
                        catch (IllegalAccessException | InvocationTargetException e)
                        {
                            throw new RuntimeException(e);
                        }
                    });
                    methodIdentify = true;
                    return executable.invoke(instance, MethodInvokeHelper.compatibleValues(args, classIds));
                }
            }
        }
        return invokeHelper.invoke(instanceOperand.calculate(contextParam), methodParams, contextParam);
    }
}
