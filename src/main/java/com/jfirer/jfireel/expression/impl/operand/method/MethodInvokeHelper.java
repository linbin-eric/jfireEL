package com.jfirer.jfireel.expression.impl.operand.method;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.jfireel.expression.Operand;

import java.lang.reflect.Executable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MethodInvokeHelper
{
    static Object compatibleValues(Object value, int classId)
    {
        switch (classId)
        {
            case ReflectUtil.CLASS_INT, ReflectUtil.PRIMITIVE_INT ->
            {
                if (value instanceof BigDecimal)
                {
                    return ((BigDecimal) value).intValue();
                }
                else if (!(value instanceof Integer))
                {
                    return ((Number) value).intValue();
                }
            }
            case ReflectUtil.CLASS_LONG, ReflectUtil.PRIMITIVE_LONG ->
            {
                if (value instanceof BigDecimal)
                {
                    return ((BigDecimal) value).longValue();
                }
                else if (!(value instanceof Long))
                {
                    return ((Number) value).longValue();
                }
            }
            case ReflectUtil.CLASS_SHORT, ReflectUtil.PRIMITIVE_SHORT ->
            {
                if (value instanceof BigDecimal)
                {
                    return ((BigDecimal) value).shortValue();
                }
                else if (!(value instanceof Short))
                {
                    return ((Number) value).shortValue();
                }
            }
            case ReflectUtil.CLASS_BYTE, ReflectUtil.PRIMITIVE_BYTE ->
            {
                if (value instanceof BigDecimal)
                {
                    return ((BigDecimal) value).byteValue();
                }
                else if (!(value instanceof Byte))
                {
                    return ((Number) value).byteValue();
                }
            }
            case ReflectUtil.CLASS_FLOAT, ReflectUtil.PRIMITIVE_FLOAT ->
            {
                if (value instanceof BigDecimal)
                {
                    return ((BigDecimal) value).floatValue();
                }
                else if (!(value instanceof Float))
                {
                    return ((Number) value).floatValue();
                }
            }
            case ReflectUtil.CLASS_DOUBLE, ReflectUtil.PRIMITIVE_DOUBLE ->
            {
                if (value instanceof BigDecimal)
                {
                    return ((BigDecimal) value).doubleValue();
                }
                else if (!(value instanceof Double))
                {
                    return ((Number) value).doubleValue();
                }
            }
            case ReflectUtil.CLASS_CHAR, ReflectUtil.PRIMITIVE_CHAR ->
            {
                if (value instanceof Character)
                {
                    return value;
                }
                else if (value instanceof String str)
                {
                    return str.charAt(0);
                }
            }
            case ReflectUtil.PRIMITIVE_BOOL, ReflectUtil.CLASS_BOOL ->
            {
                if (value instanceof Boolean)
                {
                    return value;
                }
                else if (value instanceof String str)
                {
                    return Boolean.parseBoolean(str);
                }
            }
            default -> {return value;}
        }
        return value;
    }

    static Object[] compatibleValues(Object[] values, int[] classIds)
    {
        for (int i = 0; i < values.length; i++)
        {
            Object value = values[i];
            if (value == null)
            {
                continue;
            }
            switch (classIds[i])
            {
                case ReflectUtil.CLASS_INT, ReflectUtil.PRIMITIVE_INT ->
                {
                    if (value instanceof BigDecimal)
                    {
                        values[i] = ((BigDecimal) value).intValue();
                    }
                    else if (!(value instanceof Integer))
                    {
                        values[i] = ((Number) value).intValue();
                    }
                }
                case ReflectUtil.CLASS_LONG, ReflectUtil.PRIMITIVE_LONG ->
                {
                    if (value instanceof BigDecimal)
                    {
                        values[i] = ((BigDecimal) value).longValue();
                    }
                    else if (!(value instanceof Long))
                    {
                        values[i] = ((Number) value).longValue();
                    }
                }
                case ReflectUtil.CLASS_SHORT, ReflectUtil.PRIMITIVE_SHORT ->
                {
                    if (value instanceof BigDecimal)
                    {
                        values[i] = ((BigDecimal) value).shortValue();
                    }
                    else if (!(value instanceof Short))
                    {
                        values[i] = ((Number) value).shortValue();
                    }
                }
                case ReflectUtil.CLASS_BYTE, ReflectUtil.PRIMITIVE_BYTE ->
                {
                    if (value instanceof BigDecimal)
                    {
                        values[i] = ((BigDecimal) value).byteValue();
                    }
                    else if (!(value instanceof Byte))
                    {
                        values[i] = ((Number) value).byteValue();
                    }
                }
                case ReflectUtil.CLASS_FLOAT, ReflectUtil.PRIMITIVE_FLOAT ->
                {
                    if (value instanceof BigDecimal)
                    {
                        values[i] = ((BigDecimal) value).floatValue();
                    }
                    else if (!(value instanceof Float))
                    {
                        values[i] = ((Number) value).floatValue();
                    }
                }
                case ReflectUtil.CLASS_DOUBLE, ReflectUtil.PRIMITIVE_DOUBLE ->
                {
                    if (value instanceof BigDecimal)
                    {
                        values[i] = ((BigDecimal) value).doubleValue();
                    }
                    else if (!(value instanceof Double))
                    {
                        values[i] = ((Number) value).doubleValue();
                    }
                }
                case ReflectUtil.CLASS_CHAR, ReflectUtil.PRIMITIVE_CHAR ->
                {
                    if (value instanceof Character)
                    {
                        ;
                    }
                    else if (value instanceof String str)
                    {
                        values[i] = str.charAt(0);
                    }
                }
                case ReflectUtil.CLASS_BOOL, ReflectUtil.PRIMITIVE_BOOL ->
                {
                    if (value instanceof Boolean)
                    {
                        ;
                    }
                    else if (value instanceof String str)
                    {
                        values[i] = Boolean.parseBoolean(str);
                    }
                }
            }
        }
        return values;
    }

    static boolean typeCompatibleValues(Class<?>[] parameterTypes, Object[] methodParamValues)
    {
        for (int i = 0; i < parameterTypes.length; i++)
        {
            if (methodParamValues[i] == null)
            {
                continue;
            }
            Class<?> parameterType        = parameterTypes[i];
            Class<?> methodParamValueType = methodParamValues[i].getClass();
            if (ReflectUtil.isNumberOrBigDecimal(parameterType) && ReflectUtil.isNumberOrBigDecimal(methodParamValueType))
            {
                ;
            }
            else if (ReflectUtil.isBooleanOrBooleanBox(parameterType) && ReflectUtil.isBooleanOrBooleanBox(methodParamValueType))
            {
                ;
            }
            else if (ReflectUtil.isCharOrCharBox(parameterType) && ReflectUtil.isCharOrCharBox(methodParamValueType))
            {
                ;
            }
            else if (parameterType.isAssignableFrom(methodParamValueType))
            {
                ;
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    static Executable findExecutable(List<? extends Executable> methods, Object[] methodParamValues, String memberName)
    {
        return methods.stream()//
                      .filter(executable -> executable.getName().equals(memberName))//
                      .filter(executable -> executable.getParameterCount() == methodParamValues.length)//
                      .filter(executable -> typeCompatibleValues(executable.getParameterTypes(), methodParamValues))//
                      .findAny().orElse(null);
    }

    Object invoke(Object instance, Operand[] methodParams, Map<String, Object> contextParam);
}
