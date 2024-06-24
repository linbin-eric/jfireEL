package com.jfirer.jfireel.expression;

import com.jfirer.jfireel.expression.format.FormatToken;
import com.jfirer.jfireel.expression.impl.operand.LeftAngleBracketOperand;
import com.jfirer.jfireel.expression.impl.operand.MethodInvokeOperand;
import com.jfirer.jfireel.expression.impl.operand.MethodStructureOperand;
import com.jfirer.jfireel.expression.parse.TokenParser;
import com.jfirer.jfireel.expression.parse.impl.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@Data
@Accessors(chain = true)
public class ParseContext
{
    private static TokenParser[]                                                   parsers                  = new TokenParser[]{//
            new SkipIgnoreToken(),//
            new NumberParser(),//
            new BooleanParser(),//
            new NullParser(),//
            new ExtraExecuteParser(),//
            new InnnerCallParser(),//
            new StaticClassParser(),//
            new VariableParser(),//
            new LiteralParser(),//
            new BasicOperatorParser(),//
            new LeftParenParser(),//
            new RightParenParser(),//
    };
    private        Deque<Operand>                                                  operandStack             = new LinkedList<>();
    private        Deque<Operator>                                                 operatorStack            = new LinkedList<>();
    private        Deque<Operand>                                                  processStack             = new LinkedList<>();
    private final  String                                                          el;
    private        int                                                             index;
    @Setter(AccessLevel.NONE)
    private        Map<String, Class<?>>                                           className                = new HashMap<>();
    @Setter(AccessLevel.NONE)
    private        Map<String, BiFunction<Map<String, Object>, Operand[], Object>> innerCalls               = new HashMap<>();
    private        Map<Method, MethodInvokeOperand.MethodInvokeHelper>             methodInvokeAccelerators = new HashMap<>();
    private        Map<Field, Function<Object, Object>>                            propertyReadAccelerators = new HashMap<>();
    private        Map<Expression.Tuper, MethodInvokeOperand.MethodInvokeHelper>   classExtendMethodMap     = new HashMap<>();
    private        boolean                                                         hasReturnToken           = false;

    public ParseContext(String el)
    {
        this.el = el;
    }

    public ParseContext(String el, Map<String, Class<?>> className, Map<String, BiFunction<Map<String, Object>, Operand[], Object>> innerCalls, Map<Method, MethodInvokeOperand.MethodInvokeHelper> methodInvokeAccelerators, Map<Field, Function<Object, Object>> propertyReadAccelerators, Map<Expression.Tuper, MethodInvokeOperand.MethodInvokeHelper> classExtendMethodMap)
    {
        this.el = el;
        this.className.putAll(className);
        this.innerCalls.putAll(innerCalls);
        this.methodInvokeAccelerators.putAll(methodInvokeAccelerators);
        this.propertyReadAccelerators.putAll(propertyReadAccelerators);
        this.classExtendMethodMap.putAll(classExtendMethodMap);
    }

    public void registerClass(String name, Class<?> ckass)
    {
        className.put(name, ckass);
    }

    public void registerPropertyReadAccelerator(Field field, Function<Object, Object> accelerator)
    {
        propertyReadAccelerators.put(field, accelerator);
    }

    public void registerMethodInvokeAccelerator(Method method, MethodInvokeOperand.MethodInvokeHelper helper)
    {
        methodInvokeAccelerators.put(method, helper);
    }

    List<FormatToken> formatTokens = new LinkedList<>();

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
                String trim = el.substring(oldVersionOfIndex, index).trim();
                if (trim.equals("") == false)
                {
                    formatTokens.add(FormatToken.of(trim));
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
                String trim = el.substring(oldVersionOfIndex, index).trim();
                if (trim.equals("") == false)
                {
                    formatTokens.add(FormatToken.of(trim));
                }
            }
            while (operatorStack.isEmpty() == false)
            {
                operatorStack.pop().onPop(this);
            }
        }
        catch (Throwable e)
        {
            throw new IllegalStateException("当前表达式解析出现异常，异常位置为" + el.substring(0, index) + ".[详细异常信息为:" + e.getMessage() + "]", e);
        }
        if (processStack.isEmpty() == false)
        {
            throw new IllegalStateException("当前表达式解析出现异常，异常位置为" + el.substring(0, index) + "。[详细异常信息为:表达式的解析不完整]");
        }
        while (operandStack.isEmpty() == false)
        {
            processStack.push(operandStack.pop());
        }
        if (processStack.stream().filter(operand -> operand instanceof LeftAngleBracketOperand).findAny().isPresent())
        {
            throw new IllegalStateException("当前表达式解析出现异常，代码中{}没有完全配对");
        }
        if (processStack.size() == 1 && hasReturnToken == false)
        {
            return processStack.pop();
        }
        else
        {
            return new MethodStructureOperand(processStack.toArray(Operand[]::new), true);
        }
    }
}


