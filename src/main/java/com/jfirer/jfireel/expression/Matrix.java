package com.jfirer.jfireel.expression;

import com.jfirer.jfireel.expression.impl.operand.CallOperand;
import com.jfirer.jfireel.expression.impl.operand.FunctionCallOperand;
import com.jfirer.jfireel.expression.impl.operand.InnerCallOperand;
import com.jfirer.jfireel.expression.impl.operand.ReferenceCallOperand;
import com.jfirer.jfireel.expression.impl.operand.method.MethodInvoker;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Data
@Setter(AccessLevel.NONE)
public class Matrix
{
    private final String                                       name;
    private final Matrix                                       parent;
    /**
     * 提前注册的简单类的名称
     */
    private       Map<String, Class<?>>                        className                  = new HashMap<>();
    private       Map<String, Supplier<? extends CallOperand>> callMap                    = new HashMap<>();
    /**
     * 加速方法调用的实现。对应 method 不采取反射方式调用，使用对应的MethodInvokeHelper进行调用。
     */
    private       Map<Executable, MethodInvoker>               acceleratorForMethodInvoke = new HashMap<>();
    /**
     * 加速属性的读取，对应属性的读取不采用反射的方式，采用对应的Function<Object, Object>来返回属性的值
     */
    private       Map<Field, Function<Object, Object>>         acceleratorForPropertyRead = new HashMap<>();

    public void registerClassName(String name, Class clazz)
    {
        className.put(name, clazz);
    }

    public void registerInnerCall(String name, MethodInvoker function)
    {
        callMap.put(name, () -> new InnerCallOperand(function));
    }

    public void registerReferenceCall(String name, Method method)
    {
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
        {
            callMap.put(name, ReferenceCallOperand.make(method));
        }
        else
        {
            throw new IllegalArgumentException("方法" + name + "不是静态的，或者不是public的");
        }
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
        content = content.substring(9);
        int      index             = content.indexOf("(");
        String   functionName      = content.substring(0, index).trim();
        int      index2            = content.indexOf(")");
        String   paramNameContents = content.substring(index + 1, index2);
        String[] paramNames        = Arrays.stream(paramNameContents.split(",")).map(String::trim).toArray(String[]::new);
        content = content.substring(index2 + 1).trim();
        if (content.charAt(0) != '{' || content.charAt(content.length() - 1) != '}')
        {
            throw new IllegalArgumentException("function 函数定义错误");
        }
        content = content.substring(1, content.length() - 1);
        Operand operand = Expression.parse(content, this);
        callMap.put(functionName, () -> new FunctionCallOperand(paramNames, operand));
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

    public Supplier<? extends CallOperand> findCallOperand(String name)
    {
        Supplier<? extends CallOperand> supplier = callMap.get(name);
        if (supplier != null)
        {
            return supplier;
        }
        return parent != null ? parent.findCallOperand(name) : null;
    }
}
