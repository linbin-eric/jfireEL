package com.jfireframework.jfireel.node;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.jfireframework.jfireel.CalculateType;
import com.jfireframework.jfireel.Expression;

public class VariableMethodNode implements MethodNode
{
    private String              variable;
    private String              methodName;
    private volatile Method     method;
    private List<CalculateNode> paramNodes = new ArrayList<CalculateNode>(6);
    
    public VariableMethodNode(String literals)
    {
        int index = literals.indexOf('.');
        variable = literals.substring(0, index);
        methodName = literals.substring(index + 1);
    }
    
    @Override
    public Object calculate(Map<String, Object> variables)
    {
        Object value = variables.get(variable);
        if (value == null)
        {
            return value;
        }
        Object[] args = new Object[paramNodes.size()];
        for (int i = 0; i < args.length; i++)
        {
            args[i] = paramNodes.get(i).calculate(variables);
        }
        try
        {
            return getMethod(value, args).invoke(value, args);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public CalculateType type()
    {
        return Expression.METHOD;
    }
    
    private Method getMethod(Object value, Object[] args)
    {
        if (method == null)
        {
            synchronized (this)
            {
                if (method == null)
                {
                    Class<?> ckass = value.getClass();
                    nextmethod: for (Method each : ckass.getMethods())
                    {
                        if (each.getName().equals(methodName) && each.getParameterTypes().length == args.length)
                        {
                            Class<?>[] parameterTypes = each.getParameterTypes();
                            for (int i = 0; i < args.length; i++)
                            {
                                if (args[0] != null && parameterTypes[i].isAssignableFrom(args[0].getClass()) == false)
                                {
                                    continue nextmethod;
                                }
                            }
                            method = each;
                            method.setAccessible(true);
                            break;
                        }
                    }
                }
                if (method == null)
                {
                    throw new NullPointerException();
                }
            }
        }
        return method;
    }
    
    @Override
    public void addParamNode(CalculateNode paramNode)
    {
        paramNodes.add(paramNode);
    }
}
