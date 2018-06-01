package com.jfireframework.jfireel.lexer.node.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.MethodNode;
import com.jfireframework.jfireel.lexer.token.TokenType;
import com.jfireframework.jfireel.lexer.token.Token;

public class StaticMethodNode implements MethodNode
{
	private final Class<?>	beanType;
	private volatile Method	method;
	private String			methodName;
	private CalculateNode[]	argsNodes;
	private ConvertType[]	convertTypes;
	private Token		type;
	
	public StaticMethodNode(String literals, CalculateNode beanNode)
	{
		if (beanNode.type() != Token.TYPE)
		{
			throw new IllegalArgumentException("静态方法的前面一个节点必须是类型节点");
		}
		beanType = (Class<?>) beanNode.calculate(null);
		methodName = literals;
		type = Token.METHOD;
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		Object[] args = new Object[argsNodes.length];
		try
		{
			for (int i = 0; i < args.length; i++)
			{
				args[i] = argsNodes[i].calculate(variables);
			}
			Method invoke = getMethod(args);
			MethodNodeUtil.convertArgs(args, convertTypes);
			return invoke.invoke(null, args);
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
	
	private Method getMethod(Object[] args)
	{
		if (method == null)
		{
			synchronized (this)
			{
				if (method == null)
				{
					nextmethod: for (Method each : beanType.getMethods())
					{
						if (Modifier.isStatic(each.getModifiers()) && each.getName().equals(methodName) && each.getParameterTypes().length == args.length)
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
				throw new NullPointerException("没有在类" + beanType.getName() + "找到静态方法" + methodName);
			}
		}
		return method;
	}
	
	public void setArgsNodes(CalculateNode[] argsNodes)
	{
		this.argsNodes = argsNodes;
		type = Token.METHOD_RESULT;
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
