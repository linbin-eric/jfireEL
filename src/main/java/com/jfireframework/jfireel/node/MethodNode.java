package com.jfireframework.jfireel.node;

import java.lang.reflect.Method;
import java.util.Map;
import com.jfireframework.jfireel.CalculateType;
import com.jfireframework.jfireel.Expression;

public class MethodNode implements CalculateNode
{
	private final CalculateNode	beanNode;
	private final String		methodName;
	private volatile Method		method;
	private volatile Class<?>	beanType;
	protected boolean			recognizeEveryTime	= true;
	private CalculateNode[]		argsNodes;
	private Expression			type;
	
	public MethodNode(String literals, CalculateNode beanNode)
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
		for (int i = 0; i < args.length; i++)
		{
			args[i] = argsNodes[i].calculate(variables);
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
		return type;
	}
	
	private Method getMethod(Object value, Object[] args)
	{
		if (recognizeEveryTime)
		{
			Method accessMethod = method;
			if (accessMethod == null || beanType.isAssignableFrom(value.getClass()) == false)
			{
				synchronized (this)
				{
					if ((accessMethod = method) == null || beanType.isAssignableFrom(value.getClass()) == false)
					{
						nextmethod: for (Method each : value.getClass().getMethods())
						{
							if (each.getName().equals(methodName) && each.getParameterTypes().length == args.length)
							{
								Class<?>[] parameterTypes = each.getParameterTypes();
								for (int i = 0; i < args.length; i++)
								{
									if (parameterTypes[i].isPrimitive())
									{
										if (args[0] == null || isWrapType(parameterTypes[i], args[0].getClass()) == false)
										{
											continue nextmethod;
										}
									}
									else
									{
										if (args[0] != null && parameterTypes[i].isAssignableFrom(args[0].getClass()) == false)
										{
											continue nextmethod;
										}
									}
								}
								accessMethod = each;
								accessMethod.setAccessible(true);
								method = accessMethod;
								beanType = value.getClass();
								return accessMethod;
							}
						}
						throw new NullPointerException();
					}
				}
			}
			return method;
		}
		else
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
								return method;
							}
						}
					}
					throw new NullPointerException();
				}
			}
			return method;
		}
	}
	
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
			return arge == Integer.class;
		}
		else if (primitiveType == short.class)
		{
			return arge == Short.class;
		}
		else if (primitiveType == long.class)
		{
			return arge == Long.class;
		}
		else if (primitiveType == boolean.class)
		{
			return arge == Boolean.class;
		}
		else if (primitiveType == float.class)
		{
			return arge == Float.class;
		}
		else if (primitiveType == double.class)
		{
			return arge == Double.class;
		}
		else if (primitiveType == char.class)
		{
			return arge == Character.class;
		}
		else if (primitiveType == byte.class)
		{
			return arge == Byte.class;
		}
		else
		{
			return false;
		}
	}
}
