package com.jfireframework.jfireel.node.impl;

import java.lang.reflect.Method;
import java.util.Map;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.baseutil.smc.SmcHelper;
import com.jfireframework.baseutil.smc.compiler.JavaStringCompiler;
import com.jfireframework.baseutil.smc.model.CompilerModel;
import com.jfireframework.baseutil.smc.model.MethodModel;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.MethodNode;
import com.jfireframework.jfireel.token.CalculateType;
import com.jfireframework.jfireel.token.Expression;

public class CompileMethodNode implements MethodNode
{
    public static interface Invoker
    {
        Object invoke(Object host, Object[] params);
    }
    
    private final CalculateNode beanNode;
    private final String        methodName;
    private volatile Invoker    invoker;
    private volatile Class<?>   beanType;
    protected boolean           recognizeEveryTime = true;
    private CalculateNode[]     argsNodes;
    private ConvertType[]       convertTypes;
    private Expression          type;
    
    public CompileMethodNode(String literals, CalculateNode beanNode)
    {
        methodName = literals;
        type = Expression.METHOD;
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
            convertArgs(args);
            return invoke.invoke(value, args);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    private final void convertArgs(Object[] args)
    {
        for (int i = 0; i < args.length; i++)
        {
            Object argeValue = args[i];
            switch (convertTypes[i])
            {
                case INT:
                    if (argeValue instanceof Integer == false)
                    {
                        args[i] = ((Number) argeValue).intValue();
                    }
                    break;
                case LONG:
                    if (argeValue instanceof Long == false)
                    {
                        args[i] = ((Number) argeValue).longValue();
                    }
                    break;
                case SHORT:
                    if (argeValue instanceof Short == false)
                    {
                        args[i] = ((Number) argeValue).shortValue();
                    }
                    break;
                case FLOAT:
                    if (argeValue instanceof Float == false)
                    {
                        args[i] = ((Number) argeValue).floatValue();
                    }
                    break;
                case DOUBLE:
                    if (argeValue instanceof Double == false)
                    {
                        args[i] = ((Number) argeValue).doubleValue();
                    }
                    break;
                case BYTE:
                    if (argeValue instanceof Byte == false)
                    {
                        args[i] = ((Number) argeValue).byteValue();
                    }
                    break;
                case OTHER:
                    break;
                default:
                    break;
            }
        }
    }
    
    @Override
    public CalculateType type()
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
                        nextmethod: //
                        for (Method each : value.getClass().getMethods())
                        {
                            if (each.getName().equals(methodName) && each.getParameterTypes().length == args.length)
                            {
                                Class<?>[] parameterTypes = each.getParameterTypes();
                                for (int i = 0; i < args.length; i++)
                                {
                                    if (parameterTypes[i].isPrimitive())
                                    {
                                        if (args[i] == null || isWrapType(parameterTypes[i], args[i].getClass()) == false)
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
                                buildConvertTypes(parameterTypes);
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
                        nextmethod: //
                        for (Method each : ckass.getMethods())
                        {
                            if (each.getName().equals(methodName) && each.getParameterTypes().length == args.length)
                            {
                                Class<?>[] parameterTypes = each.getParameterTypes();
                                for (int i = 0; i < args.length; i++)
                                {
                                    if (parameterTypes[i].isPrimitive())
                                    {
                                        if (args[i] == null || isWrapType(parameterTypes[i], args[i].getClass()) == false)
                                        {
                                            continue nextmethod;
                                        }
                                    }
                                    else if (args[i] != null && parameterTypes[i].isAssignableFrom(args[i].getClass()) == false)
                                    {
                                        continue nextmethod;
                                    }
                                }
                                buildConvertTypes(parameterTypes);
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
    
    private Invoker buildInvoker(Object[] args, Method each, Class<?>[] parameterTypes)
    {
        try
        {
            CompilerModel createImplClass = SmcHelper.createImplClass(Object.class, Invoker.class);
            Method declaredMethod = Invoker.class.getDeclaredMethod("invoke", Object.class, Object[].class);
            MethodModel methodModel = new MethodModel(declaredMethod);
            StringCache body = new StringCache("{ return ((" + SmcHelper.getTypeName(beanType) + ")$0)." + each.getName() + "(");
            for (int i = 0; i < args.length; i++)
            {
                switch (convertTypes[i])
                {
                    case INT:
                        body.append("(java.lang.Integer)$1[").append(i).append(']').appendComma();
                        break;
                    case LONG:
                        body.append("(java.lang.Long)$1[").append(i).append(']').appendComma();
                        break;
                    case SHORT:
                        body.append("(java.lang.Short)$1[").append(i).append(']').appendComma();
                        break;
                    case FLOAT:
                        body.append("(java.lang.Float)$1[").append(i).append(']').appendComma();
                        break;
                    case DOUBLE:
                        body.append("(java.lang.Double)$1[").append(i).append(']').appendComma();
                        break;
                    case BYTE:
                        body.append("(java.lang.Byte)$1[").append(i).append(']').appendComma();
                        break;
                    case OTHER:
                        body.append('(').append(SmcHelper.getTypeName(parameterTypes[i])).append(')').append("$1[").append(i).append(']').appendComma();
                        break;
                    default:
                        break;
                }
            }
            if (body.isCommaLast())
            {
                body.deleteLast();
            }
            body.append(");}");
            methodModel.setBody(body.toString());
            createImplClass.putMethod(methodModel);
            JavaStringCompiler compiler = new JavaStringCompiler();
            Class<?> compile = compiler.compile(createImplClass);
            return (Invoker) compile.newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private void buildConvertTypes(Class<?>[] parameterTypes)
    {
        convertTypes = new ConvertType[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++)
        {
            if (parameterTypes[i] == int.class || parameterTypes[i] == Integer.class)
            {
                convertTypes[i] = ConvertType.INT;
            }
            else if (parameterTypes[i] == short.class || parameterTypes[i] == short.class)
            {
                convertTypes[i] = ConvertType.SHORT;
            }
            else if (parameterTypes[i] == long.class || parameterTypes[i] == Long.class)
            {
                convertTypes[i] = ConvertType.LONG;
            }
            else if (parameterTypes[i] == float.class || parameterTypes[i] == Float.class)
            {
                convertTypes[i] = ConvertType.FLOAT;
            }
            else if (parameterTypes[i] == double.class || parameterTypes[i] == Double.class)
            {
                convertTypes[i] = ConvertType.DOUBLE;
            }
            else if (parameterTypes[i] == byte.class || parameterTypes[i] == Byte.class)
            {
                convertTypes[i] = ConvertType.BYTE;
            }
            else
            {
                convertTypes[i] = ConvertType.OTHER;
            }
        }
    }
    
    @Override
    public void setArgsNodes(CalculateNode[] argsNodes)
    {
        this.argsNodes = argsNodes;
        type = Expression.METHOD_RESULT;
    }
    
    @Override
    public String toString()
    {
        return "MethodNode [methodName=" + methodName + "]";
    }
    
    private boolean isWrapType(Class<?> primitiveType, Class<?> arge)
    {
        if (primitiveType == int.class)
        {
            return arge == Integer.class || arge == Long.class;
        }
        else if (primitiveType == short.class)
        {
            return arge == Integer.class || arge == Long.class;
        }
        else if (primitiveType == long.class)
        {
            return arge == Integer.class || arge == Long.class;
        }
        else if (primitiveType == boolean.class)
        {
            return arge == Boolean.class;
        }
        else if (primitiveType == float.class)
        {
            return arge == Float.class || arge == Double.class;
        }
        else if (primitiveType == double.class)
        {
            return arge == Float.class || arge == Double.class;
        }
        else if (primitiveType == char.class)
        {
            return arge == Character.class;
        }
        else if (primitiveType == byte.class)
        {
            return arge == Integer.class || arge == Long.class;
        }
        else
        {
            return false;
        }
    }
}
