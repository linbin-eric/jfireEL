package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.baseutil.smc.SmcHelper;
import com.jfirer.baseutil.smc.compiler.CompileHelper;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Data
public abstract class MethodInvokeOperand implements Operand
{
    protected final      String                                    methodName;
    protected final      Operand[]                                 methodParams;
    protected final      String                                    fragment;
    protected final      Map<Method, MethodInvokeHelper>           methodInvokeAccelerators;
    protected            ConvertType[]                             convertTypes;
    protected            Method                                    method;
    protected volatile   boolean                                   methodIdentify = false;
    private static final CompileHelper                             COMPILE_HELPER = new CompileHelper();
    private static final AtomicInteger                             COUNTER        = new AtomicInteger(1);
    private static final ConcurrentMap<Method, MethodInvokeHelper> INVOKER_MAP    = new ConcurrentHashMap<>();
    protected            MethodInvokeHelper                        invokeHelper;

    enum ConvertType
    {
        INT, LONG, SHORT, BYTE, CHAR, FLOAT, DOUBLE, BOOLEAN, NONE
    }

    protected Object methodInvoke(Object instance, Object[] methodParamValues)
    {
        for (int i = 0; i < methodParamValues.length; i++)
        {
            Object paramValue = methodParamValues[i];
            switch (convertTypes[i])
            {
                case INT ->
                {
                    if (paramValue instanceof Integer == false)
                    {
                        methodParamValues[i] = ((Number) paramValue).intValue();
                    }
                }
                case LONG ->
                {
                    if (paramValue instanceof Long == false)
                    {
                        methodParamValues[i] = ((Number) paramValue).longValue();
                    }
                }
                case SHORT ->
                {
                    if (paramValue instanceof Short == false)
                    {
                        methodParamValues[i] = ((Number) paramValue).shortValue();
                    }
                }
                case BYTE ->
                {
                    if (paramValue instanceof Byte == false)
                    {
                        methodParamValues[i] = ((Number) paramValue).byteValue();
                    }
                }
                case CHAR -> methodParamValues[i] = paramValue instanceof Character ? paramValue : ((String) paramValue).charAt(0);
                case FLOAT ->
                {
                    if (paramValue instanceof Float == false)
                    {
                        methodParamValues[i] = ((Number) paramValue).floatValue();
                    }
                }
                case DOUBLE ->
                {
                    if (paramValue instanceof Double == false)
                    {
                        methodParamValues[i] = ((Number) paramValue).doubleValue();
                    }
                }
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

    protected void findMethod(List<Method> methods, Object[] methodParamValues)
    {
        for (Method method : methods)
        {
            if (method.getName().equalsIgnoreCase(methodName) && method.getParameterCount() == methodParamValues.length)
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
                    method.setAccessible(true);
                    MethodInvokeHelper methodInvokeAccelerator = methodInvokeAccelerators.get(method);
                    if (methodInvokeAccelerator != null)
                    {
                        invokeHelper = methodInvokeAccelerator;
                    }
                    else
                    {
                        invokeHelper = (obj, methodParams, contextParam) -> {
                            Object[] _args = new Object[methodParams.length];
                            for (int i = 0; i < _args.length; i++)
                            {
                                _args[i] = methodParams[i].calculate(contextParam);
                            }
                            return methodInvoke(obj, _args);
                        };
                    }
                    this.method    = method;
                    methodIdentify = true;
                    return;
                }
            }
        }
        throw new IllegalArgumentException("解析过程中发现未能发现匹配的方法,方法名为:" + methodName + "。异常解析位置为" + fragment);
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
            if (parameterType.isPrimitive())
            {
                switch (convertTypes[i])
                {
                    case INT -> body.append("((Number)$1[").append(i).append("].calculate($2)).intValue(),");
                    case LONG -> body.append("((Number)$1[").append(i).append("].calculate($2)).longValue(),");
                    case SHORT -> body.append("((Number)$1[").append(i).append("].calculate($2)).shortValue(),");
                    case BYTE -> body.append("((Number)$1[").append(i).append("].calculate($2)).byteValue(),");
                    case CHAR -> body.append("((Character)$1[").append(i).append("].calculate($2)).charValue(),");
                    case FLOAT -> body.append("((Number)$1[").append(i).append("].calculate($2)).floatValue(),");
                    case DOUBLE -> body.append("((Number)$1[").append(i).append("].calculate($2)).doubleValue(),");
                    case BOOLEAN -> body.append("((Boolean)$1[").append(i).append("].calculate($2)).booleanValue(),");
                    case NONE -> {}
                }
            }
            else
            {
                switch (convertTypes[i])
                {
                    case INT -> body.append("convertInteger($1[").append("i").append("],$2),");
                    case LONG -> body.append("convertLong($1[").append("i").append("],$2),");
                    case SHORT -> body.append("convertShort($1[").append("i").append("],$2),");
                    case BYTE -> body.append("convertByte($1[").append("i").append("],$2),");
                    case CHAR -> body.append("convertCharacter($1[").append("i").append("],$2),");
                    case FLOAT -> body.append("convertFloat($1[").append("i").append("],$2),");
                    case DOUBLE -> body.append("convertDouble($1[").append("i").append("],$2),");
                    case BOOLEAN -> body.append("convertBoolean($1[").append("i").append("],$2),");
                    case NONE -> body.append("(").append(parameterType.getName()).append(")$1[").append(i).append("].calculate($2),");
                }
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

        public StaticMethod(Class ckass, String methodName, Operand[] methodParams, String fragment, Map<Method, MethodInvokeHelper> refenceCalls)
        {
            super(methodName, methodParams, fragment, refenceCalls);
            candidates = Stream.iterate(ckass, c -> c != Object.class, c -> c.getSuperclass()).flatMap(c -> Arrays.stream(c.getDeclaredMethods())).toList();
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            if (methodIdentify == false)
            {
                synchronized (this)
                {
                    if (methodIdentify == false)
                    {
                        Object[] methodParamValues = Arrays.stream(methodParams).map(operand -> operand.calculate(contextParam)).toArray(Object[]::new);
                        findMethod(candidates, methodParamValues);
                        return methodInvoke(null, methodParamValues);
                    }
                }
            }
            return invokeHelper.invoke(null, methodParams, contextParam);
        }
    }

    public static class InstanceMethod extends MethodInvokeOperand
    {
        private Operand instanceOperand;

        public InstanceMethod(Operand instanceOperand, String methodName, Operand[] methodParams, String fragment, Map<Method, MethodInvokeHelper> refenceCalls)
        {
            super(methodName, methodParams,  fragment, refenceCalls);
            this.instanceOperand = instanceOperand;
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            if (methodIdentify == false)
            {
                synchronized (this)
                {
                    if (methodIdentify == false)
                    {
                        Object instance = instanceOperand.calculate(contextParam);
                        if (instance == null)
                        {
                            throw new IllegalStateException("方法调用，但是调用对象为空，请检查是否变量名错误，异常位置为" + fragment);
                        }
                        Object[] args = Arrays.stream(methodParams).map(operand -> operand.calculate(contextParam)).toArray(Object[]::new);
                        findMethod(Stream.iterate((Class) instance.getClass(), c -> c != Object.class, c -> c.getSuperclass()).flatMap(c -> Arrays.stream(c.getDeclaredMethods())).toList(), args);
                        return methodInvoke(instance, args);
                    }
                }
            }
            return invokeHelper.invoke(instanceOperand.calculate(contextParam), methodParams, contextParam);
        }
    }
}
