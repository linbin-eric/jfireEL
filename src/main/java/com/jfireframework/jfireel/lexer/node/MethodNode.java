package com.jfireframework.jfireel.lexer.node;

public interface MethodNode extends CalculateNode
{
	enum ConvertType
	{
		INT, LONG, SHORT, FLOAT, DOUBLE, BYTE, BOOLEAN, CHARACTER, OTHER
	}
	
	void setArgsNodes(CalculateNode[] argsNodes);
	
	public static class MethodNodeUtil
	{
		public static final void convertArgs(Object[] args, ConvertType[] convertTypes)
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
					case CHARACTER:
					case BOOLEAN:
					case OTHER:
						// 以上三种不用转化
						break;
					default:
						break;
				}
			}
		}
		
		public static final ConvertType[] buildConvertTypes(Class<?>[] parameterTypes)
		{
			ConvertType[] convertTypes = new ConvertType[parameterTypes.length];
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
				else if (parameterTypes[i] == boolean.class || parameterTypes[i] == Boolean.class)
				{
					convertTypes[i] = ConvertType.BOOLEAN;
				}
				else
				{
					convertTypes[i] = ConvertType.OTHER;
				}
			}
			return convertTypes;
		}
		
		public static final boolean isWrapType(Class<?> primitiveType, Class<?> arge)
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
}
