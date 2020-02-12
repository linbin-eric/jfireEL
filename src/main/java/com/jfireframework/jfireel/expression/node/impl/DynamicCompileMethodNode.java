package com.jfireframework.jfireel.expression.node.impl;

import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.node.MethodNode;
import com.jfireframework.jfireel.expression.token.Token;
import com.jfireframework.jfireel.expression.token.TokenType;
import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.baseutil.smc.SmcHelper;
import com.jfirer.baseutil.smc.compiler.CompileHelper;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.MethodModel;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DynamicCompileMethodNode implements MethodNode
{
    public static interface Invoker
    {
        Object invoke(Object host, Object[] params);
    }

    private static final CompileHelper   COMPILER           = new CompileHelper();
    private static final AtomicInteger   counter            = new AtomicInteger(0);
    private final        CalculateNode   beanNode;
    private final        String          methodName;
    private volatile     Invoker         invoker;
    private volatile     Class<?>        beanType;
    protected            boolean         recognizeEveryTime = false;
    private              CalculateNode[] argsNodes;
    private              ConvertType[]   convertTypes;
    private              Token           type;

    public DynamicCompileMethodNode(String literals, CalculateNode beanNode)
    {
        methodName = literals;
        type = Token.METHOD;
        this.beanNode = beanNode;
    }

    @Override
    public Object calculate(Map<String, Object> variables)
    {
        Object value = beanNode.calculate(variables);
        if (value == null)
        {
            return value;
        }
        Object[] args = new Object[argsNodes.length];
        try
        {
            for (int i = 0; i < args.length; i++)
            {
                args[i] = argsNodes[i].calculate(variables);
            }
            Invoker invoke = getMethod(value, args);
            MethodNodeUtil.convertArgs(args, convertTypes);
            return invoke.invoke(value, args);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public TokenType type()
    {
        return type;
    }

    private Invoker getMethod(Object value, Object[] args)
    {
        if (recognizeEveryTime)
        {
            Invoker invoker = this.invoker;
            if (invoker == null || beanType.isAssignableFrom(value.getClass()) == false)
            {
                synchronized (this)
                {
                    if ((invoker = this.invoker) == null || beanType.isAssignableFrom(value.getClass()) == false)
                    {
                        nextmethod:
                        //
                        for (Method each : value.getClass().getMethods())
                        {
                            if (each.getName().equals(methodName) && each.getParameterTypes().length == args.length)
                            {
                                Class<?>[] parameterTypes = each.getParameterTypes();
                                for (int i = 0; i < args.length; i++)
                                {
                                    if (parameterTypes[i].isPrimitive())
                                    {
                                        if (args[i] == null || MethodNodeUtil.isWrapType(parameterTypes[i], args[i].getClass()) == false)
                                        {
                                            continue nextmethod;
                                        }
                                    }
                                    else
                                    {
                                        if (args[i] != null && parameterTypes[i].isAssignableFrom(args[i].getClass()) == false)
                                        {
                                            continue nextmethod;
                                        }
                                    }
                                }
                                convertTypes = MethodNodeUtil.buildConvertTypes(parameterTypes);
                                beanType = value.getClass();
                                invoker = buildInvoker(args, each, parameterTypes);
                                this.invoker = invoker;
                                return invoker;
                            }
                        }
                        throw new NullPointerException();
                    }
                }
            }
            return invoker;
        }
        else
        {
            if (invoker == null)
            {
                synchronized (this)
                {
                    if (invoker == null)
                    {
                        Class<?> ckass = value.getClass();
                        nextmethod:
                        //
                        for (Method each : ckass.getMethods())
                        {
                            if (each.getName().equals(methodName) && each.getParameterTypes().length == args.length)
                            {
                                Class<?>[] parameterTypes = each.getParameterTypes();
                                for (int i = 0; i < args.length; i++)
                                {
                                    if (parameterTypes[i].isPrimitive())
                                    {
                                        if (args[i] == null || MethodNodeUtil.isWrapType(parameterTypes[i], args[i].getClass()) == false)
                                        {
                                            continue nextmethod;
                                        }
                                    }
                                    else if (args[i] != null && parameterTypes[i].isAssignableFrom(args[i].getClass()) == false)
                                    {
                                        continue nextmethod;
                                    }
                                }
                                convertTypes = MethodNodeUtil.buildConvertTypes(parameterTypes);
                                each.setAccessible(true);
                                invoker = buildInvoker(args, each, parameterTypes);
                                return invoker;
                            }
                        }
                        throw new NullPointerException();
                    }
                }
            }
            return invoker;
        }
    }

    private Invoker buildInvoker(Object[] args, Method method, Class<?>[] parameterTypes)
    {
        try
        {
            ClassModel  classModel  = new ClassModel("Invoke_" + method.getName() + "_" + counter.incrementAndGet(), Object.class, Invoker.class);
            MethodModel methodModel = new MethodModel(classModel);
            methodModel.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
            methodModel.setMethodName("invoke");
            methodModel.setParamterTypes(Object.class, Object[].class);
            methodModel.setReturnType(Object.class);
            StringBuilder body   = new StringBuilder(" return ((" + SmcHelper.getReferenceName(method.getDeclaringClass(), classModel) + ")$0)." + method.getName() + "(");
            int           length = body.length();
            for (int i = 0; i < parameterTypes.length; i++)
            {
                Class<?> parameterType = parameterTypes[i];
                if (parameterTypes[i] == int.class || parameterTypes[i] == Integer.class)
                {
                    body.append("((java.lang.Number)$1[").append(i).append("]).intValue(),");
                }
                else if (parameterTypes[i] == short.class || parameterTypes[i] == short.class)
                {
                    body.append("((java.lang.Number)$1[").append(i).append("]).shortValue(),");
                }
                else if (parameterTypes[i] == long.class || parameterTypes[i] == Long.class)
                {
                    body.append("((java.lang.Number)$1[").append(i).append("]).longValue(),");
                }
                else if (parameterTypes[i] == float.class || parameterTypes[i] == Float.class)
                {
                    body.append("((java.lang.Number)$1[").append(i).append("]).floatValue(),");
                }
                else if (parameterTypes[i] == double.class || parameterTypes[i] == Double.class)
                {
                    body.append("((java.lang.Number)$1[").append(i).append("]).doubleValue(),");
                }
                else if (parameterTypes[i] == byte.class || parameterTypes[i] == Byte.class)
                {
                    body.append("((java.lang.Number)$1[").append(i).append("]).byteValue(),");
                }
                else if (parameterTypes[i] == boolean.class || parameterTypes[i] == Boolean.class)
                {
                    body.append("((java.lang.Boolean)$1[").append(i).append("]).booleanValue(),");
                }
                else
                {
                    body.append("((" + SmcHelper.getReferenceName(parameterTypes[i], classModel) + ")$1[").append(i).append("]),");
                }
            }
            if (body.length() != length)
            {
                body.setLength(body.length() - 1);
            }
            body.append(");");
            methodModel.setBody(body.toString());
            classModel.putMethodModel(methodModel);
            Class<?> compile = COMPILER.compile(classModel);
            return (Invoker) compile.newInstance();
        }
        catch (Exception e)
        {
            ReflectUtil.throwException(e);
            return null;
        }
    }

    @Override
    public void setArgsNodes(CalculateNode[] argsNodes)
    {
        this.argsNodes = argsNodes;
        type = Token.METHOD_RESULT;
    }

    @Override
    public void check()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public String literals()
    {
        StringBuilder cache = new StringBuilder();
        cache.append(beanNode.literals()).append('.').append(methodName).append('(');
        if (argsNodes != null)
        {
            for (CalculateNode each : argsNodes)
            {
                cache.append(each.literals()).append(',');
            }
            if (cache.charAt(cache.length() - 1) == ',')
            {
                cache.setLength(cache.length() - 1);
            }
        }
        cache.append(')');
        return cache.toString();
    }

    @Override
    public String toString()
    {
        return literals();
    }
}
