package com.jfirer.jfireel.expression;

import com.jfirer.jfireel.expression.impl.CompileStaticCallOperand;
import com.jfirer.jfireel.expression.impl.operand.FunctionCallOperand;
import com.jfirer.jfireel.expression.impl.operand.method.MethodInvoker;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Data
@Setter(AccessLevel.NONE)
public class Matrix
{
    private final String                                                 name;
    private final Matrix                                                 parent;
    /**
     * 提前注册的简单类的名称
     */
    private       Map<String, Class<?>>                                  className                  = new HashMap<>();
    /**
     * 注册的内部调用方法
     */
    private       Map<String, MethodInvoker>                             innerCalls                 = new HashMap<>();
    /**
     * 注册的自定义方法
     */
    private       Map<String, FunctionCallOperand.FunctionCallData>      functionCalls              = new HashMap<>();
    private       Map<String, Class<? extends CompileStaticCallOperand>> methodHandles              = new HashMap<>();
    /**
     * 加速方法调用的实现。对应 method 不采取反射方式调用，使用对应的MethodInvokeHelper进行调用。
     */
    private       Map<Executable, MethodInvoker>                         acceleratorForMethodInvoke = new HashMap<>();
    /**
     * 加速属性的读取，对应属性的读取不采用反射的方式，采用对应的Function<Object, Object>来返回属性的值
     */
    private       Map<Field, Function<Object, Object>>                   acceleratorForPropertyRead = new HashMap<>();

    public void registerClassName(String name, Class clazz)
    {
        className.put(name, clazz);
    }

    public void registerInnerCall(String name, MethodInvoker function)
    {
        innerCalls.put(name, function);
    }

    public void registerMethodHandle(String name, Method method)
    {
        methodHandles.put(name, CompileStaticCallOperand.make(method));
    }

    public void registerAcceleratorForPropertyRead(Field field, Function<Object, Object> accelerator)
    {
        acceleratorForPropertyRead.put(field, accelerator);
    }

    public void registerAcceleratorForMethodInvoke(Executable executable, MethodInvoker methodInvoker)
    {
        acceleratorForMethodInvoke.put(executable, methodInvoker);
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
        FunctionCallOperand.FunctionCallData data = new FunctionCallOperand.FunctionCallData();
        content = content.substring(9);
        int    index        = content.indexOf("(");
        String functionName = content.substring(0, index).trim();
        data.setFunctionName(functionName);
        int    index2     = content.indexOf(")");
        String paramNames = content.substring(index + 1, index2);
        data.setParamNames(Arrays.stream(paramNames.split(",")).map(String::trim).toArray(String[]::new));
        content = content.substring(index2 + 1).trim();
        if (content.charAt(0) != '{' || content.charAt(content.length() - 1) != '}')
        {
            throw new IllegalArgumentException("function 函数定义错误");
        }
        content = content.substring(1, content.length() - 1);
        data.setFunction(Expression.parse(content, this));
        functionCalls.put(functionName, data);
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

    public MethodInvoker findInnerCall(String name)
    {
        MethodInvoker invoker = innerCalls.get(name);
        if (invoker != null)
        {
            return invoker;
        }
        return parent != null ? parent.findInnerCall(name) : null;
    }

    public FunctionCallOperand.FunctionCallData findFunctionCall(String name)
    {
        FunctionCallOperand.FunctionCallData callData = functionCalls.get(name);
        if (callData != null)
        {
            return callData;
        }
        return parent != null ? parent.findFunctionCall(name) : null;
    }

    public Class<? extends CompileStaticCallOperand> findMethodHandle(String name)
    {
        Class<? extends CompileStaticCallOperand> ckass = methodHandles.get(name);
        if (ckass != null)
        {
            return ckass;
        }
        return parent != null ? parent.findMethodHandle(name) : null;
    }
}
