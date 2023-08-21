package com.jfirer.jfireel.expression2;

import com.jfirer.jfireel.expression2.parse.TokenParser;
import com.jfirer.jfireel.expression2.parse.impl.*;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Data
public class ParseContext
{
    private static TokenParser[]         parsers          = new TokenParser[]{//
            new SkipIgnoreToken(),//
            new NumberParser(),//
            new BooleanParser(),//
            new VarParser(),//
            new DirectMethodParser(),//
            new StaticClassParser(),//
            new VariableParser(),//
            new LiteralParser(),//
            new BasicOperatorParser(),//
            new LeftParenParser(),//
            new RightParenParser(),//玩
    };
    private        Deque<Operand>        operandStack     = new LinkedList<>();
    private        Deque<Operator>       operatorStack    = new LinkedList<>();
    private        Deque<Operand>        processStack     = new LinkedList<>();
    private final  String                el;
    private        int                   index;
    private        Map<String, Class<?>> staticClassName  = new HashMap<>();
    private        Map<String, Method>   directMethodName = new HashMap<>();

    public ParseContext(String el)
    {
        this.el = el;
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
}


