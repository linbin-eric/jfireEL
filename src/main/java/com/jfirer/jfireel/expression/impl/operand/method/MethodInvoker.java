package com.jfirer.jfireel.expression.impl.operand.method;

import com.jfirer.baseutil.STR;
import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.baseutil.smc.SmcHelper;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.ConstructorModel;
import com.jfirer.baseutil.smc.model.FieldModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import com.jfirer.jfireel.expression.ELConfig;
import com.jfirer.jfireel.expression.Operand;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

@FunctionalInterface
public interface MethodInvoker
{
    Object invoke(Object instance, Operand[] methodParams, Map<String, Object> contextParam);

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

    static boolean isTypeCompatibleValues(Class<?>[] parameterTypes, Object[] methodParamValues)
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

    static Executable findExecutable(Class clazz, Object[] methodParamValues, String memberName)
    {
        return Stream.concat(Arrays.stream(clazz.getConstructors()), Arrays.stream(clazz.getMethods()))//
                     .filter(executable -> executable.getName().equals(memberName))//
                     .filter(executable -> executable.getParameterCount() == methodParamValues.length)//
                     .filter(executable -> isTypeCompatibleValues(executable.getParameterTypes(), methodParamValues))//
                     .findAny().orElse(null);
    }

    @SneakyThrows
    static Operand make(Operand instanceOperand, Executable executable, Operand[] argOperands, int[] classIds, ELConfig elConfig)
    {
        if (instanceOperand != null && executable instanceof Method && !Modifier.isStatic(executable.getModifiers()))
        {
            ;
        }
        else if ((executable instanceof Constructor<?> || Modifier.isStatic(executable.getModifiers())) && instanceOperand == null)
        {
            ;
        }
        else
        {
            throw new IllegalArgumentException("对方法进行编译，但是入组的组合情况不明确");
        }
        ClassModel classModel = new ClassModel(STR.format("Compile{}_{}", executable instanceof Constructor<?> ? executable.getDeclaringClass().getSimpleName() : executable.getName(), Operand.COUNTER.getAndIncrement()));
        classModel.addInterface(Operand.class);
        classModel.addImport(Map.class);
        classModel.addImport(MethodInvoker.class);
        classModel.addImport(executable.getDeclaringClass());
        for (int i = 0; i < argOperands.length; i++)
        {
            classModel.addField(new FieldModel("argOperand_" + i, Operand.class, classModel));
            classModel.addField(new FieldModel("classId_" + i, int.class, classModel));
        }
        if (instanceOperand != null)
        {
            classModel.addField(new FieldModel("instanceOperand", Operand.class, classModel));
        }
        ConstructorModel constructorModel = new ConstructorModel(classModel);
        if (instanceOperand != null)
        {
            constructorModel.setParamTypes(Operand.class, Operand[].class, int[].class);
            constructorModel.setParamNames("instanceOperand", "argOperands", "classIds");
        }
        else
        {
            constructorModel.setParamTypes(Operand[].class, int[].class);
            constructorModel.setParamNames("argOperands", "classIds");
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < argOperands.length; i++)
        {
            builder.append("this.argOperand_" + i + " = argOperands[" + i + "];\r\n");
            builder.append("this.classId_" + i + " = classIds[" + i + "];\r\n");
        }
        if (instanceOperand != null)
        {
            builder.append("this.instanceOperand = instanceOperand;\r\n");
        }
        constructorModel.setBody(builder.toString());
        classModel.addConstructor(constructorModel);
        Method      calculate   = Operand.class.getDeclaredMethod("calculate", Map.class);
        MethodModel methodModel = new MethodModel(calculate, classModel);
        methodModel.setParamterNames("contextParam");
        String referenceName = SmcHelper.getReferenceName(executable.getDeclaringClass(), classModel);
        if (executable.getParameterCount() == 0)
        {
            if (instanceOperand != null)
            {
                if (ReflectUtil.getBoxedTypeOrOrigin(((Method) executable).getReturnType()) == Void.class)
                {
                    methodModel.setBody(STR.format("""
                                                           {} instance = ({}) instanceOperand.calculate(contextParam);
                                                           instance.{}();
                                                           return null;
                                                           """, referenceName, referenceName, executable.getName()));
                }
                else
                {
                    methodModel.setBody(STR.format("""
                                                           {} instance = ({}) instanceOperand.calculate(contextParam);
                                                           return instance.{}();
                                                           """, referenceName, referenceName, executable.getName()));
                }
            }
            else
            {
                if (executable instanceof Constructor)
                {
                    methodModel.setBody(STR.format("return new {}();", referenceName));
                }
                else
                {
                    methodModel.setBody(STR.format("return {}.{}();", referenceName, executable.getName()));
                }
            }
        }
        else
        {
            String format;
            if (instanceOperand != null)
            {
                if (ReflectUtil.getBoxedTypeOrOrigin(((Method) executable).getReturnType()) == Void.class)
                {
                    format = STR.format("""
                                                {} instance = ({}) instanceOperand.calculate(contextParam);
                                                instance.{}(""", referenceName, referenceName, executable.getName());
                }
                else
                {
                    format = STR.format("""
                                                {} instance = ({}) instanceOperand.calculate(contextParam);
                                                return instance.{}(""", referenceName, referenceName, executable.getName());
                }
            }
            else
            {
                if (executable instanceof Constructor<?>)
                {
                    format = STR.format("return new {}(", referenceName);
                }
                else
                {
                    if (ReflectUtil.getBoxedTypeOrOrigin(((Method) executable).getReturnType()) == Void.class)
                    {
                        format = STR.format("{}.{}(", referenceName, executable.getName());
                    }
                    else
                    {
                        format = STR.format("return {}.{}(", referenceName, executable.getName());
                    }
                }
            }
            builder = new StringBuilder();
            Class<?>[] parameterTypes = executable.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++)
            {
                if (elConfig.isDisableCompileMethodCompatibleValue())
                {
                    builder.append("(").append(SmcHelper.getReferenceName(ReflectUtil.getBoxedTypeOrOrigin(parameterTypes[i]), classModel)).append(")argOperand_").append(i).append(".calculate(contextParam)");
                }
                else
                {
                    builder.append("(").append(SmcHelper.getReferenceName(ReflectUtil.getBoxedTypeOrOrigin(parameterTypes[i]), classModel)).append(")MethodInvoker.compatibleValues(argOperand_").append(i).append(".calculate(contextParam),classId_").append(i).append(")");
                }
                if (i != parameterTypes.length - 1)
                {
                    builder.append(",");
                }
            }
            builder.append(");\r\n");
            if (executable instanceof Method method && ReflectUtil.getBoxedTypeOrOrigin(method.getReturnType()) == Void.class)
            {
                builder.append("return null;");
            }
            methodModel.setBody(format + builder);
        }
        classModel.putMethodModel(methodModel);
        Class<?> compile = Operand.COMPILE_HELPER.compile(classModel);
        if (instanceOperand != null)
        {
            return (Operand) compile.getConstructor(Operand.class, Operand[].class, int[].class).newInstance(instanceOperand, argOperands, classIds);
        }
        else
        {
            return (Operand) compile.getConstructor(Operand[].class, int[].class).newInstance(argOperands, classIds);
        }
    }
}
