package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class MethodInvokeOperand implements Operand
{
    protected Method findMethod(Class<?> ckass, String methodName, int paramCount, String fragment)
    {
        Method matchMethod = null;
        int    matchTime   = 0;
        while (ckass != Object.class)
        {
            for (Method method : ckass.getMethods())
            {
                if (method.getParameterCount() == paramCount && method.getName().equalsIgnoreCase(methodName))
                {
                    matchTime += 1;
                    matchMethod = method;
                }
            }
        }
        if (matchTime > 1)
        {
            throw new IllegalArgumentException("解析过程中发现静态类的方法有多个匹配，当前方法重载仅能支持不同入参个数的。异常解析位置为" + fragment);
        }
        if (matchTime == 0)
        {
            throw new IllegalArgumentException("解析过程中发现静态类的方法没有匹配。异常解析位置为" + fragment);
        }
        matchMethod.setAccessible(true);
        return matchMethod;
    }

    public static class StaticMethodInvokeOperand extends MethodInvokeOperand
    {
        private Method        method;
        private List<Operand> methodParams;

        public StaticMethodInvokeOperand(Class<?> ckass, String methodName, List<Operand> methodParams, String fragment)
        {
            method            = findMethod(ckass, methodName, methodParams.size(), fragment);
            this.methodParams = methodParams;
        }

        @Override
        public Object calculate(Map<String, Object> param)
        {
            Object[] array = methodParams.stream().map(operand -> operand.calculate(param)).toArray(Object[]::new);
            try
            {
                return method.invoke(null, array);
            }
            catch (Throwable e)
            {
                ReflectUtil.throwException(e);
                return null;
            }
        }
    }

    @Data
    public static class InstanceMethodInvokeOperand extends MethodInvokeOperand
    {
        private final    VariableOperand instanceOperand;
        private final    VariableOperand methodNameOperand;
        private final    List<Operand>   methodParams;
        private final    String          fragment;
        private volatile Method          method;

        @Override
        public Object calculate(Map<String, Object> param)
        {
            if (method == null)
            {
                synchronized (this)
                {
                    if (method == null)
                    {
                        Object instance = instanceOperand.calculate(param);
                        method = findMethod(instance.getClass(), methodNameOperand.getVariable(), methodParams.size(), fragment);
                        try
                        {
                            return method.invoke(instance, methodParams.stream().map(operand -> operand.calculate(param)).toArray(Object[]::new));
                        }
                        catch (Throwable e)
                        {
                            ReflectUtil.throwException(e);
                            return null;
                        }
                    }
                }
            }
            try
            {
                return method.invoke(instanceOperand.calculate(param), methodParams.stream().map(operand -> operand.calculate(param)).toArray(Object[]::new));
            }
            catch (Throwable e)
            {
                ReflectUtil.throwException(e);
                return null;
            }
        }
    }
}
