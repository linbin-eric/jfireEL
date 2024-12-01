package com.jfirer.jfireel.expression;

import com.jfirer.jfireel.PlaceHolder;
import com.jfirer.jfireel.expression.format.FormatToken;
import com.jfirer.jfireel.expression.impl.operand.FunctionCallOperand;
import com.jfirer.jfireel.expression.impl.operand.MethodStructureOperand;
import com.jfirer.jfireel.expression.impl.operand.method.MethodInvoker;
import com.jfirer.jfireel.expression.parse.TokenParser;
import com.jfirer.jfireel.expression.parse.impl.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

@Data
@Accessors(chain = true)
public class ParseContext
{
    private static TokenParser[]                        parsers                  = new TokenParser[]{//
            new SkipIgnoreToken(),//
            new NumberParser(),//
            new BooleanParser(),//
            new NullParser(),//
            new ExtraExecuteParser(),//
            new InnnerCallOrFunctionCallParser(),//
            new StaticClassParser(),//
            new VariableParser(),//
            new LiteralParser(),//
            new BasicOperatorParser(),//
            new LeftParenParser(),//
            new RightParenParser(),//
    };
    private        Deque<Operand>                       operandStack             = new LinkedList<>();
    private        Deque<Operator>                      operatorStack            = new LinkedList<>();
    private        Deque<Operand>                       processStack             = new LinkedList<>();
    /**
     * 只增加，每次识别到一个内容就往里添加
     */
    @Setter(AccessLevel.NONE)
    private        Deque<Object>                        recognizeToken           = new LinkedList<>();
    private final  String                               el;
    private        int                                  index;
    @Setter(AccessLevel.NONE)
    private        Map<String, Class<?>>                className                = new HashMap<>();
    @Setter(AccessLevel.NONE)
    private        Map<String, MethodInvoker>           innerCalls               = new HashMap<>();
    private        Map<String, FunctionCallOperand>     funcationCalls           = new HashMap<>();
    private        Map<Executable, MethodInvoker>       methodInvokeAccelerators = new HashMap<>();
    private        Map<Field, Function<Object, Object>> propertyReadAccelerators = new HashMap<>();
    private        boolean                              hasReturnToken           = false;
    private        ELConfig                             config;

    public ParseContext(String el)
    {
        this(el, ELConfig.DEFAULT_CONFIG);
    }

    public ParseContext(String el, ELConfig elConfig)
    {
        this.el     = el;
        this.config = elConfig;
        this.className.putAll(Expression.className);
        this.innerCalls.putAll(Expression.innerCalls);
        this.funcationCalls.putAll(Expression.FUNCTION_CALL_OPERAND_MAP);
        this.methodInvokeAccelerators.putAll(Expression.methodInvokeAccelerators);
        this.propertyReadAccelerators.putAll(Expression.propertyReadAccelerators);
    }

    public void registerClass(String name, Class<?> ckass)
    {
        className.put(name, ckass);
    }

    public void registerPropertyReadAccelerator(Field field, Function<Object, Object> accelerator)
    {
        propertyReadAccelerators.put(field, accelerator);
    }

    public void registerMethodInvokeAccelerator(Method method, MethodInvoker helper)
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
        if (processStack.stream().filter(operand -> operand == PlaceHolder.LEFT_BRACE).findAny().isPresent())
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


