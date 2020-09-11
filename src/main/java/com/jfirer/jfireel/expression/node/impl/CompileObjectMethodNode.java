package com.jfirer.jfireel.expression.node.impl;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.node.MethodNode;
import com.jfirer.jfireel.expression.token.Token;
import com.jfirer.jfireel.expression.token.TokenType;
import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.baseutil.smc.SmcHelper;
import com.jfirer.baseutil.smc.compiler.CompileHelper;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.MethodModel;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CompileObjectMethodNode implements MethodNode
{
    public interface Invoker
    {
        Object invoke(Object host, Object[] params);
    }

    private static final CompileHelper   COMPILER = new CompileHelper();
    private static final AtomicInteger   counter  = new AtomicInteger(0);
    private final        CalculateNode   beanNode;
    private final        String          methodName;
    private volatile     Invoker         invoker;
    private volatile     Class<?>        beanType;
    protected final      boolean         recognizeEveryTime;
    private CalculateNode[] argsNodes;
    private Token           type;

    public CompileObjectMethodNode(String literals, CalculateNode beanNode, boolean recognizeEveryTime)
    {
        methodName = literals;
        type = Token.METHOD;
        this.beanNode = beanNode;
        this.recognizeEveryTime = recognizeEveryTime;
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
            return invoke.invoke(value, args);
        }
        catch (Exception e)
        {
            ReflectUtil.throwException(e);
            return null;
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
                        invoker = this.invoker = buildInvoker(value, args);
                        return invoker;
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
                        invoker = buildInvoker(value, args);
                        return invoker;
                    }
                }
            }
            return invoker;
        }
    }

    private Invoker buildInvoker(Object value, Object[] args)
    {
        nextmethod:
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
                beanType = value.getClass();
                return buildInvoker(args, each);
            }
        }
        ReflectUtil.throwException(new NullPointerException());
        return null;
    }

    private Invoker buildInvoker(Object[] args, Method method)
    {
        try
        {
            ClassModel  classModel  = new ClassModel("Invoke_" + method.getName() + "_" + counter.incrementAndGet(), Object.class, Invoker.class);
            MethodModel methodModel = new MethodModel(classModel);
            methodModel.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
            methodModel.setMethodName("invoke");
            methodModel.setParamterTypes(Object.class, Object[].class);
            methodModel.setReturnType(Object.class);
            StringBuilder body           = new StringBuilder(" return ((" + SmcHelper.getReferenceName(method.getDeclaringClass(), classModel) + ")$0)." + method.getName() + "(");
            int           length         = body.length();
            Class<?>[]    parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++)
            {
                Class<?> parameterType = parameterTypes[i];
                if (parameterType == int.class)
                {
                    body.append("((java.lang.Number)$1[").append(i).append("]).intValue(),");
                }
                else if (parameterType == Integer.class)
                {
                    if (args[i].getClass() == Integer.class)
                    {
                        body.append("((java.lang.Integer)$").append(i).append("),");
                    }
                    else
                    {
                        body.append("((java.lang.Number)$1[").append(i).append("]).intValue(),");
                    }
                }
                else if (parameterTypes[i] == short.class || parameterTypes[i] == short.class)
                {
                    body.append("((java.lang.Number)$1[").append(i).append("]).shortValue(),");
                }
                else if (parameterTypes[i] == long.class)
                {
                    body.append("((java.lang.Number)$1[").append(i).append("]).longValue(),");
                }
                else if (parameterType == Long.class)
                {
                    if (args[i].getClass() == Long.class)
                    {
                        body.append("((java.lang.Long)$").append(i).append("),");
                    }
                    else
                    {
                        body.append("((java.lang.Number)$1[").append(i).append("]).longValue(),");
                    }
                }
                else if (parameterTypes[i] == float.class)
                {
                    body.append("((java.lang.Number)$1[").append(i).append("]).floatValue(),");
                }
                else if (parameterType == Float.class)
                {
                    if (args[i].getClass() == Float.class)
                    {
                        body.append("((java.lang.Float)$").append(i).append("),");
                    }
                    else
                    {
                        body.append("((java.lang.Number)$1[").append(i).append("]).floatValue(),");
                    }
                }
                else if (parameterTypes[i] == double.class)
                {
                    body.append("((java.lang.Number)$1[").append(i).append("]).doubleValue(),");
                }
                else if (parameterType == Double.class)
                {
                    body.append("((java.lang.Double)$").append(i).append("),");
                }
                else if (parameterTypes[i] == byte.class || parameterTypes[i] == Byte.class)
                {
                    body.append("((java.lang.Number)$1[").append(i).append("]).byteValue(),");
                }
                else if (parameterTypes[i] == boolean.class)
                {
                    body.append("((java.lang.Boolean)$1[").append(i).append("]).booleanValue(),");
                }
                else if (parameterType == Boolean.class)
                {
                    body.append("((java.lang.Boolean)$").append(i).append("),");
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
