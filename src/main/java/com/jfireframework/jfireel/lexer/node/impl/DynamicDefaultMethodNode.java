package com.jfireframework.jfireel.lexer.node.impl;

import java.lang.reflect.Method;
import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.MethodNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;

public class DynamicDefaultMethodNode implements MethodNode
{
	private final CalculateNode	beanNode;
	private final String		methodName;
	private volatile Method		method;
	private volatile Class<?>	beanType;
	protected boolean			recognizeEveryTime	= true;
	private CalculateNode[]		argsNodes;
	private ConvertType[]		convertTypes;
	private Expression			type;
	
	public DynamicDefaultMethodNode(String literals, CalculateNode beanNode)
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
			Method invoke = getMethod(value, args);
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
								accessMethod = each;
								accessMethod.setAccessible(true);
								beanType = value.getClass();
								method = accessMethod;
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
								method = each;
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

	@Override
	public void check()
	{
		// TODO Auto-generated method stub
		
	}
	
}
