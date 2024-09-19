package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.baseutil.smc.SmcHelper;
import com.jfirer.baseutil.smc.compiler.CompileHelper;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.Operand;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Data
public abstract class MethodInvokeOperand implements Operand
{
    protected final      String                                    memberName;
    protected final      Operand[]                                 methodParams;
    protected final      String                                    fragment;
    protected final      Map<Method, MethodInvokeHelper>           methodInvokeAccelerators;
    protected            int[]                                     convertTypes;
    protected            Executable                                candidate;
    protected volatile   boolean                                   methodIdentify = false;
    private static final CompileHelper                             COMPILE_HELPER = new CompileHelper();
    private static final AtomicInteger                             COUNTER        = new AtomicInteger(1);
    private static final ConcurrentMap<Method, MethodInvokeHelper> INVOKER_MAP    = new ConcurrentHashMap<>();
    protected            MethodInvokeHelper                        invokeHelper;

    protected Object[] compatibleValues(Object[] values, int[] classIds)
    {
        for (int i = 0; i < values.length; i++)
        {
            Object value = values[i];
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

    protected Object[] candidateParamValues(Object[] methodParamValues)
    {
        for (int i = 0; i < methodParamValues.length; i++)
        {
            Object paramValue = methodParamValues[i];
            switch (convertTypes[i])
            {
                case ReflectUtil.CLASS_INT, ReflectUtil.PRIMITIVE_INT ->
                {
                    if (paramValue instanceof BigDecimal)
                    {
                        methodParamValues[i] = ((BigDecimal) paramValue).intValue();
                    }
                    if (!(paramValue instanceof Integer))
                    {
                        methodParamValues[i] = ((Number) paramValue).intValue();
                    }
                }
                case ReflectUtil.CLASS_LONG, ReflectUtil.PRIMITIVE_LONG ->
                {
                    if (paramValue instanceof BigDecimal)
                    {
                        methodParamValues[i] = ((BigDecimal) paramValue).longValue();
                    }
                    if (!(paramValue instanceof Long))
                    {
                        methodParamValues[i] = ((Number) paramValue).longValue();
                    }
                }
                case ReflectUtil.CLASS_SHORT, ReflectUtil.PRIMITIVE_SHORT ->
                {
                    if (paramValue instanceof BigDecimal)
                    {
                        methodParamValues[i] = ((BigDecimal) paramValue).shortValue();
                    }
                    if (!(paramValue instanceof Short))
                    {
                        methodParamValues[i] = ((Number) paramValue).shortValue();
                    }
                }
                case ReflectUtil.CLASS_BYTE, ReflectUtil.PRIMITIVE_BYTE ->
                {
                    if (paramValue instanceof BigDecimal)
                    {
                        methodParamValues[i] = ((BigDecimal) paramValue).byteValue();
                    }
                    if (!(paramValue instanceof Byte))
                    {
                        methodParamValues[i] = ((Number) paramValue).byteValue();
                    }
                }
                case ReflectUtil.CLASS_CHAR, ReflectUtil.PRIMITIVE_CHAR -> methodParamValues[i] = paramValue instanceof Character ? paramValue : ((String) paramValue).charAt(0);
                case ReflectUtil.CLASS_FLOAT, ReflectUtil.PRIMITIVE_FLOAT ->
                {
                    if (paramValue instanceof BigDecimal)
                    {
                        methodParamValues[i] = ((BigDecimal) paramValue).floatValue();
                    }
                    if (!(paramValue instanceof Float))
                    {
                        methodParamValues[i] = ((Number) paramValue).floatValue();
                    }
                }
                case ReflectUtil.CLASS_DOUBLE, ReflectUtil.PRIMITIVE_DOUBLE ->
                {
                    if (paramValue instanceof BigDecimal)
                    {
                        methodParamValues[i] = ((BigDecimal) paramValue).doubleValue();
                    }
                    if (!(paramValue instanceof Double))
                    {
                        methodParamValues[i] = ((Number) paramValue).doubleValue();
                    }
                }
            }
        }
        return methodParamValues;
    }

    public static boolean typeCompatibleValues(Class<?>[] parameterTypes, Object[] methodParamValues)
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

    protected Executable findExecutable(List<? extends Executable> methods, Object[] methodParamValues, String memberName)
    {
        return methods.stream()//
                      .filter(executable -> executable.getName().equals(memberName))//
                      .filter(executable -> executable.getParameterCount() == methodParamValues.length)//
                      .filter(executable -> typeCompatibleValues(executable.getParameterTypes(), methodParamValues))//
                      .findAny().orElse(null);
    }

    protected boolean findMethod(List<? extends Executable> methods, Object[] methodParamValues, String memberName)
    {
        Optional<? extends Executable> first = methods.stream()//
                                                      .filter(executable -> executable.getName().equals(memberName))//
                                                      .filter(executable -> executable.getParameterCount() == methodParamValues.length)//
                                                      .filter(executable -> typeCompatibleValues(executable.getParameterTypes(), methodParamValues))//
                                                      .findFirst();
        if (first.isEmpty())
        {
            return false;
        }
        Executable executable = first.get();
        convertTypes = Arrays.stream(executable.getParameterTypes()).mapToInt(ReflectUtil::getClassId).toArray();
        executable.setAccessible(true);
        if (executable instanceof Method method)
        {
            invokeHelper = methodInvokeAccelerators.getOrDefault(executable, (obj, methodParams, contextParam) -> {
                Object[] _args = new Object[methodParams.length];
                for (int i = 0; i < _args.length; i++)
                {
                    _args[i] = methodParams[i].calculate(contextParam);
                }
                try
                {
                    return method.invoke(obj, candidateParamValues(_args));
                }
                catch (IllegalAccessException | InvocationTargetException e)
                {
                    throw new RuntimeException(e);
                }
            });
        }
        else if (executable instanceof Constructor<?> constructor)
        {
            invokeHelper = (obj, methodParams, contextParam) -> {
                Object[] _args = new Object[methodParams.length];
                for (int i = 0; i < _args.length; i++)
                {
                    _args[i] = methodParams[i].calculate(contextParam);
                }
                try
                {
                    return constructor.newInstance(candidateParamValues(_args));
                }
                catch (IllegalAccessException | InvocationTargetException | InstantiationException e)
                {
                    throw new RuntimeException(e);
                }
            };
        }
        this.candidate = executable;
        methodIdentify = true;
        return true;
    }

    public interface MethodInvokeHelper
    {
        Object invoke(Object instance, Operand[] methodParams, Map<String, Object> contextParam);

        default Integer convertInteger(Operand operand, Map<String, Object> contextParam)
        {
            Object value = operand.calculate(contextParam);
            if (value == null)
            {
                return null;
            }
            else if (value instanceof BigDecimal)
            {
                return ((BigDecimal) value).intValue();
            }
            else if (value instanceof Integer)
            {
                return (Integer) value;
            }
            else
            {
                return ((Number) value).intValue();
            }
        }

        default Long convertLong(Operand operand, Map<String, Object> contextParam)
        {
            Object value = operand.calculate(contextParam);
            if (value == null)
            {
                return null;
            }
            else if (value instanceof BigDecimal)
            {
                return ((BigDecimal) value).longValue();
            }
            else if (value instanceof Long)
            {
                return (Long) value;
            }
            else
            {
                return ((Number) value).longValue();
            }
        }

        default Short convertShort(Operand operand, Map<String, Object> contextParam)
        {
            Object value = operand.calculate(contextParam);
            if (value == null)
            {
                return null;
            }
            else if (value instanceof BigDecimal)
            {
                return ((BigDecimal) value).shortValue();
            }
            else if (value instanceof Short)
            {
                return (Short) value;
            }
            else
            {
                return ((Number) value).shortValue();
            }
        }

        default Byte convertByte(Operand operand, Map<String, Object> contextParam)
        {
            Object value = operand.calculate(contextParam);
            if (value == null)
            {
                return null;
            }
            else if (value instanceof BigDecimal)
            {
                return ((BigDecimal) value).byteValue();
            }
            else if (value instanceof Byte)
            {
                return (Byte) value;
            }
            else
            {
                return ((Number) value).byteValue();
            }
        }

        default Float convertFloat(Operand operand, Map<String, Object> contextParam)
        {
            Object value = operand.calculate(contextParam);
            if (value == null)
            {
                return null;
            }
            else if (value instanceof BigDecimal)
            {
                return ((BigDecimal) value).floatValue();
            }
            else if (value instanceof Float)
            {
                return (Float) value;
            }
            else
            {
                return ((Number) value).floatValue();
            }
        }

        default Double convertDouble(Operand operand, Map<String, Object> contextParam)
        {
            Object value = operand.calculate(contextParam);
            if (value == null)
            {
                return null;
            }
            else if (value instanceof BigDecimal)
            {
                return ((BigDecimal) value).doubleValue();
            }
            else if (value instanceof Double)
            {
                return (Double) value;
            }
            else
            {
                return ((Number) value).doubleValue();
            }
        }

        default Boolean convertBoolean(Operand operand, Map<String, Object> contextParam)
        {
            Object value = operand.calculate(contextParam);
            if (value == null)
            {
                return null;
            }
            else if (value instanceof Boolean)
            {
                return (Boolean) value;
            }
            else
            {
                throw new IllegalArgumentException("参数类型应该是 Boolean");
            }
        }

        default Character convertCharacter(Operand operand, Map<String, Object> contextParam)
        {
            Object value = operand.calculate(contextParam);
            if (value == null)
            {
                return null;
            }
            else if (value instanceof Character)
            {
                return (Character) value;
            }
            else
            {
                throw new IllegalArgumentException("参数类型应该是 Char");
            }
        }
    }

    protected MethodInvokeHelper findInvoker(Method method)
    {
        return INVOKER_MAP.computeIfAbsent(method, this::buildInvoker);
    }

    protected MethodInvokeHelper buildInvoker(Method method)
    {
        ClassModel classModel = new ClassModel("CompileMethodInvoker_" + method.getName() + "_" + COUNTER.incrementAndGet(), Object.class, MethodInvokeHelper.class);
        classModel.addImport(Number.class);
        classModel.addImport(Character.class);
        classModel.addImport(Boolean.class);
        MethodModel methodModel = new MethodModel(classModel);
        methodModel.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
        methodModel.setMethodName("invoke");
        methodModel.setParamterTypes(Object.class, Operand[].class, Map.class);
        methodModel.setReturnType(Object.class);
        StringBuilder body;
        if (Modifier.isStatic(method.getModifiers()))
        {
            body = new StringBuilder(" return (" + SmcHelper.getReferenceName(method.getDeclaringClass(), classModel) + ")." + method.getName() + "(");
        }
        else
        {
            body = new StringBuilder(" return ((" + SmcHelper.getReferenceName(method.getDeclaringClass(), classModel) + ")$0)." + method.getName() + "(");
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++)
        {
            Class<?> parameterType = parameterTypes[i];
            switch (convertTypes[i])
            {
                case ReflectUtil.PRIMITIVE_INT -> body.append("((Number)$1[").append(i).append("].calculate($2)).intValue(),");
                case ReflectUtil.PRIMITIVE_LONG -> body.append("((Number)$1[").append(i).append("].calculate($2)).longValue(),");
                case ReflectUtil.PRIMITIVE_SHORT -> body.append("((Number)$1[").append(i).append("].calculate($2)).shortValue(),");
                case ReflectUtil.PRIMITIVE_BYTE -> body.append("((Number)$1[").append(i).append("].calculate($2)).byteValue(),");
                case ReflectUtil.PRIMITIVE_CHAR -> body.append("((Character)$1[").append(i).append("].calculate($2)).charValue(),");
                case ReflectUtil.PRIMITIVE_FLOAT -> body.append("((Number)$1[").append(i).append("].calculate($2)).floatValue(),");
                case ReflectUtil.PRIMITIVE_DOUBLE -> body.append("((Number)$1[").append(i).append("].calculate($2)).doubleValue(),");
                case ReflectUtil.PRIMITIVE_BOOL -> body.append("((Boolean)$1[").append(i).append("].calculate($2)).booleanValue(),");
                case ReflectUtil.CLASS_INT -> body.append("convertInteger($1[").append("i").append("],$2),");
                case ReflectUtil.CLASS_LONG -> body.append("convertLong($1[").append("i").append("],$2),");
                case ReflectUtil.CLASS_SHORT -> body.append("convertShort($1[").append("i").append("],$2),");
                case ReflectUtil.CLASS_BYTE -> body.append("convertByte($1[").append("i").append("],$2),");
                case ReflectUtil.CLASS_CHAR -> body.append("convertCharacter($1[").append("i").append("],$2),");
                case ReflectUtil.CLASS_FLOAT -> body.append("convertFloat($1[").append("i").append("],$2),");
                case ReflectUtil.CLASS_DOUBLE -> body.append("convertDouble($1[").append("i").append("],$2),");
                case ReflectUtil.CLASS_BOOL -> body.append("convertBoolean($1[").append("i").append("],$2),");
                default -> body.append("(").append(parameterType.getName()).append(")$1[").append(i).append("].calculate($2),");
            }
        }
        if (parameterTypes.length != 0)
        {
            body.setLength(body.length() - 1);
        }
        body.append(");");
        methodModel.setBody(body.toString());
        classModel.putMethodModel(methodModel);
        Class<?> compile = null;
        try
        {
            compile = COMPILE_HELPER.compile(classModel);
            return (MethodInvokeHelper) compile.newInstance();
        }
        catch (Throwable e)
        {
            ReflectUtil.throwException(e);
            return null;
        }
    }

    public static class StaticMethod extends MethodInvokeOperand
    {
        private final List<Method> candidates;

        public StaticMethod(Class ckass, String methodName, Operand[] methodParams, String fragment, Map<Method, MethodInvokeHelper> methodInvokeAccelerators)
        {
            super(methodName, methodParams, fragment, methodInvokeAccelerators);
            candidates = Stream.iterate(ckass, c -> c != Object.class, Class::getSuperclass).flatMap(c -> Arrays.stream(c.getDeclaredMethods())).toList();
        }

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
                        if (!findMethod(candidates, methodParamValues, memberName))
                        {
                            throw new IllegalArgumentException("解析过程中发现未能发现匹配的方法,方法名为:" + memberName + "。异常解析位置为" + fragment);
                        }
                        try
                        {
                            return ((Method) candidate).invoke(null, candidateParamValues(methodParamValues));
                        }
                        catch (IllegalAccessException | InvocationTargetException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            return invokeHelper.invoke(null, methodParams, contextParam);
        }
    }

    public static class ConstructorMethod extends MethodInvokeOperand
    {
        private Class                ckass;
        private List<Constructor<?>> candidates;

        public ConstructorMethod(Class<?> ckass, Operand[] methodParams, String fragment, Map<Method, MethodInvokeHelper> methodInvokeAccelerators)
        {
            super(ckass.getName(), methodParams, fragment, methodInvokeAccelerators);
            this.ckass = ckass;
            candidates = Arrays.stream(ckass.getConstructors()).filter(constructor -> constructor.getParameterCount() == methodParams.length).toList();
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
                        Executable  executable = findExecutable(List.of(ckass.getConstructors()), args, memberName);
                        final int[] classIds   = Arrays.stream(executable.getParameterTypes()).mapToInt(ReflectUtil::getClassId).toArray();
                        if (executable == null)
                        {
                            throw new IllegalArgumentException("解析过程中发现未能发现匹配的构造方法。异常解析位置为" + fragment);
                        }
                        Constructor constructor = (Constructor) executable;
                        invokeHelper = (obj, argOperands, context) -> {
                            Object[] _args = new Object[argOperands.length];
                            for (int i = 0; i < _args.length; i++)
                            {
                                _args[i] = argOperands[i].calculate(contextParam);
                            }
                            try
                            {
                                return constructor.newInstance(compatibleValues(_args, classIds));
                            }
                            catch (IllegalAccessException | InvocationTargetException | InstantiationException e)
                            {
                                throw new RuntimeException(e);
                            }
                        };
                        return ((Constructor<?>) executable).newInstance(compatibleValues(args, classIds));
                    }
                }
            }
            return invokeHelper.invoke(null, methodParams, contextParam);
        }
    }

    public static class InstanceMethod extends MethodInvokeOperand
    {
        private Operand                                   instanceOperand;
        private Map<Expression.Tuper, MethodInvokeHelper> classExtendMethodMap;

        public InstanceMethod(Operand instanceOperand, String methodName, Operand[] methodParams, String fragment, Map<Method, MethodInvokeHelper> methodInvokeAccelerators, Map<Expression.Tuper, MethodInvokeHelper> classExtendMethodMap)
        {
            super(methodName, methodParams, fragment, methodInvokeAccelerators);
            this.instanceOperand      = instanceOperand;
            this.classExtendMethodMap = classExtendMethodMap;
        }

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
                        invokeHelper = classExtendMethodMap.get(new Expression.Tuper(instance.getClass(), memberName));
                        if (invokeHelper != null)
                        {
                            return invokeHelper.invoke(instance, methodParams, contextParam);
                        }
                        Object[] args = Arrays.stream(methodParams).map(operand -> operand.calculate(contextParam)).toArray(Object[]::new);

                        Method executable = (Method) findExecutable(Stream.iterate((Class) instance.getClass(), c -> c != Object.class, Class::getSuperclass).flatMap(c -> Arrays.stream(c.getDeclaredMethods())).toList(), args, memberName);
                        final int[] classIds= Arrays.stream(executable.getParameterTypes()).mapToInt(ReflectUtil::getClassId).toArray();
                        if (executable==null)
                        {
                            throw new IllegalArgumentException("解析过程中发现未能发现匹配的方法,方法名为:" + memberName + "。异常解析位置为" + fragment);
                        }
                        invokeHelper = methodInvokeAccelerators.getOrDefault(executable, (obj, argOperands, context) -> {
                            Object[] _args = new Object[argOperands.length];
                            for (int i = 0; i < _args.length; i++)
                            {
                                _args[i] = argOperands[i].calculate(context);
                            }
                            try
                            {
                                return executable.invoke(obj, compatibleValues(_args,classIds));
                            }
                            catch (IllegalAccessException | InvocationTargetException e)
                            {
                                throw new RuntimeException(e);
                            }
                        });
                        try
                        {
                            return executable.invoke(instance, compatibleValues(args,classIds));
                        }
                        catch (IllegalAccessException | InvocationTargetException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            return invokeHelper.invoke(instanceOperand.calculate(contextParam), methodParams, contextParam);
        }
    }
}
