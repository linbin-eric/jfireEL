package com.jfirer.jfireel.expression;

import com.jfirer.jfireel.expression.impl.operand.CallOperand;
import com.jfirer.jfireel.expression.impl.operand.FunctionCallOperand;
import com.jfirer.jfireel.expression.impl.operand.ReferenceCallOperand;
import com.jfirer.jfireel.expression.impl.operand.method.MethodInvoker;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;

@Data
@Setter(AccessLevel.NONE)
public class Matrix
{
    private final String                                         name;
    private final Matrix                                         parent;
    /**
     * 提前注册的简单类的名称
     */
    private       Map<String, Class<?>>                          className                  = new HashMap<>();
    private       Map<String, List<CallOperand.CallOperandData>> callMap                    = new HashMap<>();
    /**
     * 加速方法调用的实现。对应 method 不采取反射方式调用，使用对应的MethodInvokeHelper进行调用。
     */
    private       Map<Executable, MethodInvoker>                 acceleratorForMethodInvoke = new HashMap<>();
    /**
     * 加速属性的读取，对应属性的读取不采用反射的方式，采用对应的Function<Object, Object>来返回属性的值
     */
    private       Map<Field, Function<Object, Object>>           acceleratorForPropertyRead = new HashMap<>();

    public void registerClassName(String name, Class clazz)
    {
        className.put(name, clazz);
    }

    static boolean matchType(Object[] argValues, Class[] parameterTypes)
    {
        for (int i = 0; i < parameterTypes.length; i++)
        {
            if (parameterTypes[i].isPrimitive())
            {
                if (argValues[i] == null)
                {
                    return false;
                }
                if ((argValues[i].getClass() == Integer.class && parameterTypes[i] == int.class)//
                    || (argValues[i].getClass() == Long.class && parameterTypes[i] == long.class)//
                    || (argValues[i].getClass() == Double.class && parameterTypes[i] == double.class)//
                    || (argValues[i].getClass() == Float.class && parameterTypes[i] == float.class)//
                    || (argValues[i].getClass() == Short.class && parameterTypes[i] == short.class)//
                    || (argValues[i].getClass() == Byte.class && parameterTypes[i] == byte.class)//
                    || (argValues[i].getClass() == Character.class && parameterTypes[i] == char.class)//
                    || (argValues[i].getClass() == Boolean.class && parameterTypes[i] == boolean.class)//
                )
                {
                    ;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                if (argValues[i] == null)
                {
                    ;
                }
                else if ((argValues[i].getClass() == Integer.class && (parameterTypes[i] == int.class || parameterTypes[i] == Integer.class))//
                         || (argValues[i].getClass() == Long.class && (parameterTypes[i] == long.class || parameterTypes[i] == Long.class))//
                         || (argValues[i].getClass() == Double.class && (parameterTypes[i] == double.class || parameterTypes[i] == Double.class))//
                         || (argValues[i].getClass() == Float.class && (parameterTypes[i] == float.class || parameterTypes[i] == Float.class))//
                         || (argValues[i].getClass() == Short.class && (parameterTypes[i] == short.class || parameterTypes[i] == Short.class))//
                         || (argValues[i].getClass() == Byte.class && (parameterTypes[i] == byte.class || parameterTypes[i] == Byte.class))//
                         || (argValues[i].getClass() == Character.class && (parameterTypes[i] == char.class || parameterTypes[i] == Character.class))//
                         || (argValues[i].getClass() == Boolean.class && (parameterTypes[i] == boolean.class || parameterTypes[i] == Boolean.class))//
                         || parameterTypes[i].isAssignableFrom(argValues[i].getClass()))
                {
                    ;
                }
                else
                {
                    return false;
                }
            }
        }
        return true;
    }

    public void registerAcceleratorForPropertyRead(Field field, Function<Object, Object> accelerator)
    {
        acceleratorForPropertyRead.put(field, accelerator);
    }

    public void registerAcceleratorForMethodInvoke(Executable executable, MethodInvoker methodInvoker)
    {
        acceleratorForMethodInvoke.put(executable, methodInvoker);
    }

    public void registerReferenceCall(String name, Method method)
    {
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
        {
            List<CallOperand.CallOperandData> list = callMap.computeIfAbsent(name, k -> new LinkedList<>());
            if (method.getParameterCount() > 0)
            {
                list.add(new CallOperand.CallOperandData().setName(name).setParamCount(method.getParameterCount())//
                                                          .setParameterTypes(method.getParameterTypes())//
                                                          .setSupportVariableParams(method.getParameterTypes()[method.getParameterCount() - 1].isArray()).setConstructor(args -> ReferenceCallOperand.make(method, args)));
            }
            else
            {
                list.add(new CallOperand.CallOperandData().setName(name).setParamCount(0)//
                                                          .setParameterTypes(method.getParameterTypes())//
                                                          .setSupportVariableParams(false).setConstructor(args -> ReferenceCallOperand.make(method, args)));
            }
        }
        else
        {
            throw new IllegalArgumentException("方法" + name + "不是静态的，或者不是public的");
        }
    }

    public MethodInvoker findAcceleratorForMethodInvoke(Executable executable)
    {
        MethodInvoker methodInvoker = acceleratorForMethodInvoke.get(executable);
        if (methodInvoker != null)
        {
            return methodInvoker;
        }
        return parent != null ? parent.findAcceleratorForMethodInvoke(executable) : null;
    }

    public Function<Object, Object> findAcceleratorForPropertyRead(Field field)
    {
        Function<Object, Object> function = acceleratorForPropertyRead.get(field);
        if (function != null)
        {
            return function;
        }
        return parent != null ? parent.findAcceleratorForPropertyRead(field) : null;
    }

    public Class findClassByName(String name)
    {
        Class<?> clazz = className.get(name);
        if (clazz != null)
        {
            return clazz;
        }
        return parent != null ? parent.findClassByName(name) : null;
    }

    public boolean existCallOperand(String name)
    {
        return callMap.containsKey(name);
    }

    public void registerFunctionCall(String content)
    {
        content = content.trim();
        while (content.charAt(0) == '#')
        {
            int i = content.indexOf("\n");
            if (i == -1)
            {
                throw new IllegalArgumentException("#符号并未单独占据一行，错误");
            }
            content = content.substring(i + 1);
        }
        if (!content.startsWith("function "))
        {
            throw new IllegalArgumentException("function 函数定义错误");
        }
        content = content.substring(9);
        int    index             = content.indexOf("(");
        String functionName      = content.substring(0, index).trim();
        int    index2            = content.indexOf(")");
        String paramNameContents = content.substring(index + 1, index2);
        record ParamEntry(String name, Class type)
        {
        }
        ;
        ParamEntry[] paramEntries = Arrays.stream(paramNameContents.split(",")).map(String::trim)//
                                          .map(split -> {
                                              String[] s = split.split(" ");
                                              if (s[0].equals("Object..."))
                                              {
                                                  ParamEntry entry = new ParamEntry(s[1], Object[].class);
                                                  return entry;
                                              }
                                              else
                                              {
                                                  Class classByName = findClassByName(s[0]);
                                                  if (classByName == null)
                                                  {
                                                      throw new NullPointerException("类型名称:" + s[0] + "没有注册，无法在注册方法：" + functionName + "引用");
                                                  }
                                                  ParamEntry entry = new ParamEntry(s[1], classByName);
                                                  return entry;
                                              }
                                          })//
                                          .toArray(ParamEntry[]::new);
        content = content.substring(index2 + 1).trim();
        if (content.charAt(0) != '{' || content.charAt(content.length() - 1) != '}')
        {
            throw new IllegalArgumentException("function 函数定义错误");
        }
        content = content.substring(1, content.length() - 1);
        Operand                           operand = Expression.parse(content, this);
        List<CallOperand.CallOperandData> list    = callMap.computeIfAbsent(functionName, k -> new LinkedList<>());
        if (paramEntries.length != 0 && paramEntries[paramEntries.length - 1].type == Object[].class)
        {
            list.add(new CallOperand.CallOperandData().setName(functionName).setSupportVariableParams(true).setParamCount(paramEntries.length)//
                                                      .setParameterTypes(Arrays.stream(paramEntries).map(ParamEntry::type).toArray(Class[]::new))//
                                                      .setConstructor(args -> new FunctionCallOperand(Arrays.stream(paramEntries).map(ParamEntry::name).toArray(String[]::new), operand, true).setArgs(args)));
        }
        else
        {
            list.add(new CallOperand.CallOperandData().setName(functionName).setSupportVariableParams(false).setParamCount(paramEntries.length)//
                                                      .setParameterTypes(Arrays.stream(paramEntries).map(ParamEntry::type).toArray(Class[]::new))//
                                                      .setConstructor(args -> new FunctionCallOperand(Arrays.stream(paramEntries).map(ParamEntry::name).toArray(String[]::new), operand, false).setArgs(args)));
        }
    }

    public CallOperand findCallOperand(String name, Operand[] args, Object[] argValues)
    {
        List<CallOperand.CallOperandData> list = callMap.get(name);
        if (list != null)
        {
            List<CallOperand.CallOperandData> result = list.stream().filter(c -> c.isSupportVariableParams() == false)//
                                                           .filter(c -> c.getParamCount() == args.length)//
                                                           .filter(c -> matchType(argValues, c.getParameterTypes()))//
                                                           .toList();
            if (result.size() > 1)
            {
                throw new IllegalArgumentException("对调用:" + name + "进行方法定位，结果多于1个。");
            }
            else if (result.size() == 1)
            {
                return result.get(0).getConstructor().apply(args);
            }
            result = list.stream().filter(c -> c.isSupportVariableParams())//
                         .filter(c -> c.getParamCount() <= args.length)//
                         .filter(c -> matchType(Arrays.copyOf(argValues, c.getParamCount() - 1), Arrays.copyOf(c.getParameterTypes(), c.getParamCount() - 1)))//
                         .toList();
            if (result.size() > 1)
            {
                throw new IllegalArgumentException("对调用:" + name + "进行方法定位，结果多于1个。");
            }
            else if (result.size() == 1)
            {
                return result.get(0).getConstructor().apply(args);
            }
        }
        return parent != null ? parent.findCallOperand(name, args, argValues) : null;
    }
}
