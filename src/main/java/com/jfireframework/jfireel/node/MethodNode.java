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
	private ConvertType[]		convertTypes;
	private Expression			type;
	
	enum ConvertType
	{
		INT, LONG, SHORT, FLOAT, DOUBLE, BYTE, OTHER
	}
	
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
		try
		{
			for (int i = 0; i < args.length; i++)
			{
				args[i] = argsNodes[i].calculate(variables);
			}
			Method invoke = getMethod(value, args);
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
