package com.jfireframework.jfireel.expression;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.jfireframework.jfireel.exception.UnParsedException;
import com.jfireframework.jfireel.expression.node.CalculateNode;
import com.jfireframework.jfireel.expression.parse.Invoker;
import com.jfireframework.jfireel.expression.parse.impl.CommaParser;
import com.jfireframework.jfireel.expression.parse.impl.ConstantStringParser;
import com.jfireframework.jfireel.expression.parse.impl.EnumParser;
import com.jfireframework.jfireel.expression.parse.impl.IdentifierParser;
import com.jfireframework.jfireel.expression.parse.impl.LeftBracketParser;
import com.jfireframework.jfireel.expression.parse.impl.LeftParenParser;
import com.jfireframework.jfireel.expression.parse.impl.MethodParser;
import com.jfireframework.jfireel.expression.parse.impl.NodeParser;
import com.jfireframework.jfireel.expression.parse.impl.NumberParser;
import com.jfireframework.jfireel.expression.parse.impl.OperatorParser;
import com.jfireframework.jfireel.expression.parse.impl.PropertyParser;
import com.jfireframework.jfireel.expression.parse.impl.RightBracketParser;
import com.jfireframework.jfireel.expression.parse.impl.RightParenParser;
import com.jfireframework.jfireel.expression.parse.impl.SkipIgnoredToken;
import com.jfireframework.jfireel.expression.parse.impl.TypeParser;
import com.jfireframework.jfireel.expression.util.OperatorResultUtil;

public class Expression
{
    private CalculateNode        parseNode;
    private Deque<CalculateNode> nodes = new LinkedList<CalculateNode>();
    private String               el;
    private int                  function;
    private Invoker              head;
    private static final Invoker DEFAULT_HEAD;
    static
    {
        NodeParser[] parsers = new NodeParser[] { //
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
        Invoker pred = new Invoker() {
            
            @Override
            public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
            {
                return offset;
            }
        };
        for (int i = parsers.length - 1; i > -1; i--)
        {
            final NodeParser parser = parsers[i];
            final Invoker next = pred;
            Invoker invoker = new Invoker() {
                
                @Override
                public int parse(String el, int offset, Deque<CalculateNode> nodes, int function)
                {
                    return parser.parse(el, offset, nodes, function, next);
                }
            };
            pred = invoker;
        }
        DEFAULT_HEAD = pred;
    }
    
    public static Expression parse(String el)
    {
        return new Expression(el, 0, DEFAULT_HEAD);
    }
    
    public static Expression parse(String el, int function)
    {
        return new Expression(el, function, DEFAULT_HEAD);
    }
    
    public static Expression parse(String el, int function, Invoker head)
    {
        return new Expression(el, function, head);
    }
    
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
        CalculateNode tmp;
        while ((tmp = nodes.pollFirst()) != null)
        {
            list.add(0, tmp);
        }
        parseNode = OperatorResultUtil.aggregate(list, function, el, offset);
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
