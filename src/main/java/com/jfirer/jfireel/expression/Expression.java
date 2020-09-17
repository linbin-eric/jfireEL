package com.jfirer.jfireel.expression;

import com.jfirer.jfireel.exception.UnParsedException;
import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.parse.Invoker;
import com.jfirer.jfireel.expression.parse.impl.*;
import com.jfirer.jfireel.expression.util.Functional;
import com.jfirer.jfireel.expression.util.OperatorResultUtil;

import java.util.*;

public class Expression
{
    private static final Invoker              DEFAULT_HEAD;

    static
    {
        NodeParser[] parsers = new NodeParser[]{ //
                new SkipIgnoredToken(), //
                new LeftParenParser(), //
                new RightParenParser(), //
                new LeftBracketParser(), //
                new TypeParser(), //
                new RightBracketParser(), //
                new PropertyParser(), //
                new EnumParser(), //
                new MethodParser(), //
                new CommaParser(), //
                new ConstantStringParser(), //
                new NumberParser(), //
                new IdentifierParser(), //
                new OperatorParser()//
        };
        Invoker pred = (el, offset, nodes, function) -> offset;
        for (int i = parsers.length - 1; i > -1; i--)
        {
            final NodeParser parser = parsers[i];
            final Invoker    next   = pred;
            Invoker invoker = (el, offset, nodes, function) -> parser.parse(el, offset, nodes, function, next);
            pred = invoker;
        }
        DEFAULT_HEAD = pred;
    }

    private              CalculateNode        parseNode;
    private              Deque<CalculateNode> nodes = new LinkedList<CalculateNode>();
    private       String  el;
    private final int     function;
    private final Invoker head;

    private Expression(String el, int function, Invoker head)
    {
        this.head = head;
        this.el = el;
        this.function = function;
        try
        {
            scan();
        }
        catch (Exception e)
        {
            throw new UnParsedException(el, e);
        }
    }

    public static Expression parse(String el)
    {
        return new Expression(el, Functional.build().setMethodInvokeByCompile(true).toFunction(), DEFAULT_HEAD);
    }

    public static Expression parse(String el, int function)
    {
        return new Expression(el, function, DEFAULT_HEAD);
    }

    public static Expression parse(String el, int function, Invoker head)
    {
        return new Expression(el, function, head);
    }

    private void scan()
    {
        int offset = 0;
        int length = el.length();
        while (offset < length)
        {
            int result = head.parse(el, offset, nodes, function);
            if (result == offset)
            {
                throw new IllegalArgumentException("无法识别的表达式，解析过程预见无法识别的字符:" + el.substring(0, offset));
            }
            offset = result;
        }
        List<CalculateNode> list = new ArrayList<CalculateNode>();
        CalculateNode       tmp;
        while ((tmp = nodes.pollFirst()) != null)
        {
            list.add(0, tmp);
        }
        parseNode = OperatorResultUtil.aggregate(list, el, offset);
        nodes = null;
        el = null;
    }

    @SuppressWarnings("unchecked")
    public <T> T calculate(Map<String, Object> variables)
    {
        return (T) parseNode.calculate(variables);
    }

    @SuppressWarnings("unchecked")
    public <T> T calculate()
    {
        return (T) parseNode.calculate(null);
    }

    public CalculateNode parseResult()
    {
        return parseNode;
    }

    /**
     * 返回解析的表达式
     *
     * @return
     */
    public String getEl()
    {
        return el;
    }
}
