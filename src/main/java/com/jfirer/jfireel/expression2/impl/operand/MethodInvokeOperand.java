package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
public abstract class MethodInvokeOperand implements Operand
{
    protected          String        fragment;
    protected          String        methodName;
    protected          List<Operand> methodParams;
    protected          ConvertType[] convertTypes;
    protected volatile Method        method;

    enum ConvertType
    {
        INT, LONG, SHORT, BYTE, CHAR, FLOAT, DOUBLE, BOOLEAN, NONE
    }

    protected Object methodInvoke(Object instance, Object[] methodParamValues)
    {
        for (int i = 0; i < methodParamValues.length; i++)
        {
            switch (convertTypes[i])
            {
                case INT -> methodParamValues[i] = ((Number) methodParamValues[i]).intValue();
                case LONG -> methodParamValues[i] = ((Number) methodParamValues[i]).longValue();
                case SHORT -> methodParamValues[i] = ((Number) methodParamValues[i]).shortValue();
                case BYTE -> methodParamValues[i] = ((Number) methodParamValues[i]).byteValue();
                case CHAR -> methodParamValues[i] = methodParamValues[i] instanceof Character ? methodParamValues[i] : ((String) methodParamValues[i]).charAt(0);
                case FLOAT -> methodParamValues[i] = ((Number) methodParamValues[i]).floatValue();
                case DOUBLE -> methodParamValues[i] = ((Number) methodParamValues[i]).doubleValue();
                case BOOLEAN, NONE -> {}
            }
        }
        try
        {
            return method.invoke(instance, methodParamValues);
        }
        catch (Throwable e)
        {
            ReflectUtil.throwException(e);
            return null;
        }
    }

    protected void findMethod(Class<?> ckass, String methodName, Object[] methodParamValues)
    {
        while (ckass != Object.class)
        {
            for (Method method : ckass.getDeclaredMethods())
            {
                if (method.getParameterCount() == methodParamValues.length && method.getName().equals(methodName))
                {
                    boolean allTypeMatch = true;
                    for (int i = 0; i < method.getParameterTypes().length; i++)
                    {
                        Class<?> parameterType    = method.getParameterTypes()[i];
                        Object   methodParamValue = methodParamValues[i];
                        if (parameterType.isPrimitive())
                        {
                            if (parameterType == float.class || parameterType == double.class)
                            {
                                if (methodParamValues[i] != null && (methodParamValue.getClass() == Float.class || methodParamValue.getClass() == Double.class))
                                {
                                    ;
                                }
                                else
                                {
                                    allTypeMatch = false;
                                    break;
                                }
                            }
                            else if (parameterType == boolean.class)
                            {
                                if (methodParamValue != null && methodParamValue.getClass() == Boolean.class)
                                {
                                    ;
                                }
                                else
                                {
                                    allTypeMatch = false;
                                    break;
                                }
                            }
                            else if (parameterType == char.class)
                            {
                                if (methodParamValue != null && methodParamValue.getClass() == Character.class)
                                {
                                    ;
                                }
                                else
                                {
                                    allTypeMatch = false;
                                    break;
                                }
                            }
                            else
                            {
                                if (methodParamValue != null && (methodParamValue.getClass() == Integer.class || methodParamValue.getClass() == Long.class || methodParamValue.getClass() == Byte.class || methodParamValue.getClass() == Short.class))
                                {
                                    ;
                                }
                                else
                                {
                                    allTypeMatch = false;
                                }
                            }
                        }
                        else if (Number.class.isAssignableFrom(parameterType))
                        {
                            if (parameterType == Float.class || parameterType == Double.class)
                            {
                                if (methodParamValue == null || (methodParamValue.getClass() == Float.class || methodParamValue == Double.class))
                                {
                                    ;
                                }
                                else
                                {
                                    allTypeMatch = false;
                                }
                            }
                            else
                            {
                                if (methodParamValue == null || (methodParamValue.getClass() == Integer.class || methodParamValue.getClass() == Long.class || methodParamValue.getClass() == Byte.class || methodParamValue.getClass() == Short.class))
                                {
                                    ;
                                }
                                else
                                {
                                    allTypeMatch = false;
                                }
                            }
                        }
                        else if (Boolean.class.isAssignableFrom(parameterType))
                        {
                            if (methodParamValue == null || methodParamValue.getClass() == Boolean.class)
                            {
                                ;
                            }
                            else
                            {
                                allTypeMatch = false;
                                break;
                            }
                        }
                        else if (parameterType == Character.class)
                        {
                            if (methodParamValue == null || (methodParamValue.getClass() == Character.class || methodParamValue.getClass() == String.class))
                            {
                                ;
                            }
                            else
                            {
                                allTypeMatch = false;
                                break;
                            }
                        }
                        else if (methodParamValue == null || parameterType.isAssignableFrom(methodParamValue.getClass()))
                        {
                            ;
                        }
                        else
                        {
                            allTypeMatch = false;
                            break;
                        }
                    }
                    if (allTypeMatch)
                    {
                        convertTypes = Arrays.stream(method.getParameterTypes()).map(type -> {
                            if (type == int.class || type == Integer.class)
                            {
                                return ConvertType.INT;
                            }
                            else if (type == short.class || type == Short.class)
                            {
                                return ConvertType.SHORT;
                            }
                            else if (type == long.class || type == Long.class)
                            {
                                return ConvertType.LONG;
                            }
                            else if (type == float.class || type == Float.class)
                            {
                                return ConvertType.FLOAT;
                            }
                            else if (type == double.class || type == Double.class)
                            {
                                return ConvertType.DOUBLE;
                            }
                            else if (type == byte.class || type == Byte.class)
                            {
                                return ConvertType.BYTE;
                            }
                            else if (type == char.class || type == Character.class)
                            {
                                return ConvertType.CHAR;
                            }
                            else if (type == boolean.class || type == Boolean.class)
                            {
                                return ConvertType.BOOLEAN;
                            }
                            else
                            {
                                return ConvertType.NONE;
                            }
                        }).toArray(ConvertType[]::new);
                        this.method  = method;
                        return;
                    }
                }
            }
            ckass = ckass.getSuperclass();
        }
        throw new IllegalArgumentException("解析过程中发现未能发现匹配的方法对象。异常解析位置为" + fragment);
    }

    public static class StaticMethod extends MethodInvokeOperand
    {
        private Class<?> ckass;

        public StaticMethod(Class<?> ckass, String methodName, List<Operand> methodParams, String fragment)
        {
            this.ckass        = ckass;
            this.methodName   = methodName;
            this.methodParams = methodParams;
            this.fragment     = fragment;
        }

        @Override
        public Object calculate(Map<String, Object> param)
        {
            if (method == null)
            {
                synchronized (this)
                {
                    if (method == null)
                    {
                        Object[] methodParamValues = methodParams.stream().map(operand -> operand.calculate(param)).toArray(Object[]::new);
                        findMethod(ckass, methodName, methodParamValues);
                        return methodInvoke(null, methodParamValues);
                    }
                }
            }
            return methodInvoke(null, methodParams.stream().map(operand -> operand.calculate(param)).toArray(Object[]::new));
        }
    }

    @Data
    public static class InstanceMethod extends MethodInvokeOperand
    {
        private Operand instanceOperand;

        public InstanceMethod(Operand instanceOperand, String methodName, List<Operand> methodParams, String fragment)
        {
            this.instanceOperand = instanceOperand;
            this.methodName      = methodName;
            this.methodParams    = methodParams;
            this.fragment        = fragment;
        }

        @Override
        public Object calculate(Map<String, Object> param)
        {
            if (method == null)
            {
                synchronized (this)
                {
                    if (method == null)
                    {
                        Object   instance          = instanceOperand.calculate(param);
                        Object[] methodParamValues = methodParams.stream().map(operand -> operand.calculate(param)).toArray(Object[]::new);
                        findMethod(instance.getClass(), methodName, methodParamValues);
                        return methodInvoke(instance, methodParamValues);
                    }
                }
            }
            return methodInvoke(instanceOperand.calculate(param), methodParams.stream().map(operand -> operand.calculate(param)).toArray(Object[]::new));
        }
    }

    @Data
    public static class DirectMethod extends MethodInvokeOperand
    {
        private final List<Method> candidates;

        public DirectMethod(List<Method> candidates)
        {
            this.candidates = candidates;
        }

        @Override
        public Object calculate(Map<String, Object> param)
        {
            if (method == null)
            {
                synchronized (this)
                {
                    if (method == null)
                    {
                        Object[] methodParamValues = methodParams.stream().map(operand -> operand.calculate(param)).toArray(Object[]::new);
                        findMethod(candidates, methodParamValues, fragment);
                        return methodInvoke(null, methodParamValues);
                    }
                }
            }
            return methodInvoke(null, methodParams.stream().map(operand -> operand.calculate(param)).toArray(Object[]::new));
        }

        protected void findMethod(List<Method> methods, Object[] methodParamValues, String fragment)
        {
            for (Method method : methods)
            {
                if (method.getParameterCount() == methodParamValues.length)
                {
                    boolean allTypeMatch = true;
                    for (int i = 0; i < method.getParameterTypes().length; i++)
                    {
                        Class<?> parameterType    = method.getParameterTypes()[i];
                        Object   methodParamValue = methodParamValues[i];
                        if (parameterType.isPrimitive())
                        {
                            if (parameterType == float.class || parameterType == double.class)
                            {
                                if (methodParamValues[i] != null && (methodParamValue.getClass() == Float.class || methodParamValue.getClass() == Double.class))
                                {
                                    ;
                                }
                                else
                                {
                                    allTypeMatch = false;
                                    break;
                                }
                            }
                            else if (parameterType == boolean.class)
                            {
                                if (methodParamValue != null && methodParamValue.getClass() == Boolean.class)
                                {
                                    ;
                                }
                                else
                                {
                                    allTypeMatch = false;
                                    break;
                                }
                            }
                            else if (parameterType == char.class)
                            {
                                if (methodParamValue != null && methodParamValue.getClass() == Character.class)
                                {
                                    ;
                                }
                                else
                                {
                                    allTypeMatch = false;
                                    break;
                                }
                            }
                            else
                            {
                                if (methodParamValue != null && (methodParamValue.getClass() == Integer.class || methodParamValue.getClass() == Long.class || methodParamValue.getClass() == Byte.class || methodParamValue.getClass() == Short.class))
                                {
                                    ;
                                }
                                else
                                {
                                    allTypeMatch = false;
                                }
                            }
                        }
                        else if (Number.class.isAssignableFrom(parameterType))
                        {
                            if (parameterType == Float.class || parameterType == Double.class)
                            {
                                if (methodParamValue == null || (methodParamValue.getClass() == Float.class || methodParamValue == Double.class))
                                {
                                    ;
                                }
                                else
                                {
                                    allTypeMatch = false;
                                }
                            }
                            else
                            {
                                if (methodParamValue == null || (methodParamValue.getClass() == Integer.class || methodParamValue.getClass() == Long.class || methodParamValue.getClass() == Byte.class || methodParamValue.getClass() == Short.class))
                                {
                                    ;
                                }
                                else
                                {
                                    allTypeMatch = false;
                                }
                            }
                        }
                        else if (Boolean.class.isAssignableFrom(parameterType))
                        {
                            if (methodParamValue == null || methodParamValue.getClass() == Boolean.class)
                            {
                                ;
                            }
                            else
                            {
                                allTypeMatch = false;
                                break;
                            }
                        }
                        else if (parameterType == Character.class)
                        {
                            if (methodParamValue == null || (methodParamValue.getClass() == Character.class || methodParamValue.getClass() == String.class))
                            {
                                ;
                            }
                            else
                            {
                                allTypeMatch = false;
                                break;
                            }
                        }
                        else if (methodParamValue == null || parameterType.isAssignableFrom(methodParamValue.getClass()))
                        {
                            ;
                        }
                        else
                        {
                            allTypeMatch = false;
                            break;
                        }
                    }
                    if (allTypeMatch)
                    {
                        convertTypes = Arrays.stream(method.getParameterTypes()).map(type -> {
                            if (type == int.class || type == Integer.class)
                            {
                                return ConvertType.INT;
                            }
                            else if (type == short.class || type == Short.class)
                            {
                                return ConvertType.SHORT;
                            }
                            else if (type == long.class || type == Long.class)
                            {
                                return ConvertType.LONG;
                            }
                            else if (type == float.class || type == Float.class)
                            {
                                return ConvertType.FLOAT;
                            }
                            else if (type == double.class || type == Double.class)
                            {
                                return ConvertType.DOUBLE;
                            }
                            else if (type == byte.class || type == Byte.class)
                            {
                                return ConvertType.BYTE;
                            }
                            else if (type == char.class || type == Character.class)
                            {
                                return ConvertType.CHAR;
                            }
                            else if (type == boolean.class || type == Boolean.class)
                            {
                                return ConvertType.BOOLEAN;
                            }
                            else
                            {
                                return ConvertType.NONE;
                            }
                        }).toArray(ConvertType[]::new);
                        this.method  = method;
                        return;
                    }
                }
            }
            throw new IllegalArgumentException("解析过程中发现未能发现匹配的直接方法对象。异常解析位置为" + fragment);
        }
    }
}
