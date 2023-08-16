package com.jfirer.jfireel.expression2;

import com.jfirer.jfireel.expression2.parse.TokenParser;
import com.jfirer.jfireel.expression2.parse.impl.*;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.*;

@Data
public class ParseContext
{
    private static TokenParser[]             parsers          = new TokenParser[]{//
            new SkipIgnoreToken(),//
            new NumberParser(),//
            new DirectMethodParser(),//
            new StaticClassParser(),//
            new VariableParser(),//
            new LiteralParser(),//
            new BasicOperatorParser(),//
    };
    private        Deque<Operand>            operandStack     = new LinkedList<>();
    private        Deque<Operator>           operatorStack    = new LinkedList<>();
    private        Deque<Operand>            processStack     = new LinkedList<>();
    private final  String                    el;
    private        int                       index;
    private        Map<String, Class<?>>     staticClassName  = new HashMap<>();
    private        Map<String, Method> directMethodName = new HashMap<>();

    public ParseContext(String el)
    {
        this.el = el;
    }

    public void parse()
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
    }
}


