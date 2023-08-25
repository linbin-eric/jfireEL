package com.jfirer.jfireel.expression2;

import com.jfirer.jfireel.expression2.impl.operand.MutliOperand;
import com.jfirer.jfireel.expression2.parse.TokenParser;
import com.jfirer.jfireel.expression2.parse.impl.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class ParseContext
{
    private static TokenParser[]             parsers         = new TokenParser[]{//
            new SkipIgnoreToken(),//
            new NumberParser(),//
            new BooleanParser(),//
            new ExtraExecuteParser(),//
            new DirectMethodParser(),//
            new StaticClassParser(),//
            new VariableParser(),//
            new LiteralParser(),//
            new BasicOperatorParser(),//
            new LeftParenParser(),//
            new RightParenParser(),//玩
    };
    private        Deque<Operand>            operandStack    = new LinkedList<>();
    private        Deque<Operator>           operatorStack   = new LinkedList<>();
    private        Deque<Operand>            processStack    = new LinkedList<>();
    private final  String                    el;
    private        int                       index;
    @Setter(AccessLevel.NONE)
    private        Map<String, Class<?>>     staticClassName = new HashMap<>();
    @Setter(AccessLevel.NONE)
    private        Map<String, List<Method>> directMethods   = new HashMap<>();

    public ParseContext(String el)
    {
        this.el = el;
    }

    public ParseContext(String el, Map<String, Class<?>> staticClassName, Map<String, List<Method>> directMethods)
    {
        this.el = el;
        this.staticClassName.putAll(staticClassName);
        this.directMethods.putAll(directMethods);
    }

    public void registerClass(String name, Class<?> ckass)
    {
        staticClassName.put(name, ckass);
    }

    public void registerMethod(String name, Method method)
    {
        List<Method> methods = directMethods.computeIfAbsent(name, s -> new CopyOnWriteArrayList<>());
        methods.add(method);
    }

    public void registerMethod(Method method)
    {
        registerMethod(method.getName(), method);
    }

    public Operand parse()
    {
        try
        {
            int length = el.length();
            while (index != length)
            {
                int oldVersionOfIndex = index;
                for (TokenParser each : parsers)
                {
                    if (each.parse(this))
                    {
                        break;
                    }
                }
                if (oldVersionOfIndex == index)
                {
                    throw new IllegalStateException("无法解析表达式，当前解析进度为:" + el.substring(0, oldVersionOfIndex));
                }
            }
            while (operatorStack.isEmpty() == false)
            {
                operatorStack.pop().onPop(this);
            }
        }
        catch (Throwable e)
        {
            throw new IllegalStateException("当前表达式解析出现异常，异常位置为" + el.substring(0, index), e);
        }
        if (operandStack.size() == 1)
        {
            return operandStack.pop();
        }
        else
        {
            throw new IllegalStateException("解析表达式异常，解析完毕后剩余的操作数大于 1 个");
        }
    }

    public Operand parseMutli()
    {
        try
        {
            int length = el.length();
            while (index != length)
            {
                int oldVersionOfIndex = index;
                for (TokenParser each : parsers)
                {
                    if (each.parse(this))
                    {
                        break;
                    }
                }
                if (oldVersionOfIndex == index)
                {
                    throw new IllegalStateException("无法解析表达式，当前解析进度为:" + el.substring(0, oldVersionOfIndex));
                }
            }
            while (operatorStack.isEmpty() == false)
            {
                operatorStack.pop().onPop(this);
            }
        }
        catch (Throwable e)
        {
            throw new IllegalStateException("当前表达式解析出现异常，异常位置为" + el.substring(0, index), e);
        }
        if (processStack.isEmpty() == false)
        {
            throw new IllegalStateException("当前表达式解析出现异常，异常位置为" + el.substring(0, index));
        }
        while (operandStack.isEmpty() == false)
        {
            processStack.push(operandStack.pop());
        }
        return new MutliOperand(processStack.toArray(Operand[]::new));
    }
}


