package com.jfireframework.jfireel.lexer.node.impl;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.baseutil.smc.SmcHelper;
import com.jfireframework.baseutil.smc.compiler.JavaStringCompiler;
import com.jfireframework.baseutil.smc.model.ClassModel;
import com.jfireframework.baseutil.smc.model.MethodModel;
import com.jfireframework.baseutil.smc.model.MethodModel.AccessLevel;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.node.MethodNode;
import com.jfireframework.jfireel.lexer.token.Token;
import com.jfireframework.jfireel.lexer.token.TokenType;

public class DynamicCompileMethodNode implements MethodNode
{
	public static interface Invoker
	{
		Object invoke(Object host, Object[] params);
	}
	
	private static final JavaStringCompiler	COMPILER			= new JavaStringCompiler();
	private static final AtomicInteger		counter				= new AtomicInteger(0);
	private final CalculateNode				beanNode;
	private final String					methodName;
	private volatile Invoker				invoker;
	private volatile Class<?>				beanType;
	protected boolean						recognizeEveryTime	= true;
	private CalculateNode[]					argsNodes;
	private ConvertType[]					convertTypes;
	private Token							type;
	
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
			ClassModel classModel = new ClassModel("Invoke_" + method.getName() + "_" + counter.incrementAndGet(), Object.class, Invoker.class);
			MethodModel methodModel = new MethodModel(classModel);
			methodModel.setAccessLevel(AccessLevel.PUBLIC);
			methodModel.setMethodName("invoke");
			methodModel.setParamterTypes(Object.class, Object[].class);
			methodModel.setReturnType(Object.class);
			StringCache body = new StringCache("{ return ((" + SmcHelper.getTypeName(method.getDeclaringClass()) + ")$0)." + method.getName() + "(");
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
					case BOOLEAN:
						body.append("(java.lang.Boolean)$1[").append(i).append(']').appendComma();
						break;
					case CHARACTER:
						body.append("(java.lang.Character)$1[").append(i).append(']').appendComma();
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
			classModel.putMethodModel(methodModel);
			Class<?> compile = COMPILER.compile(classModel);
			return (Invoker) compile.newInstance();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
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
