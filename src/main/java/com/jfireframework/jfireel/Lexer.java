package com.jfireframework.jfireel;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.jfireframework.baseutil.StringUtil;
import com.jfireframework.jfireel.node.BuildInNodeFactory;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.MethodNode;
import com.jfireframework.jfireel.node.NodeFactory;
import com.jfireframework.jfireel.node.QuestionNode;
import com.jfireframework.jfireel.token.CalculateType;
import com.jfireframework.jfireel.token.DefaultKeyWord;
import com.jfireframework.jfireel.token.Expression;
import com.jfireframework.jfireel.token.Operator;
import com.jfireframework.jfireel.token.Symbol;
import com.jfireframework.jfireel.util.CharType;

public class Lexer
{
    private CalculateNode        parseNode;
    private Deque<CalculateNode> nodes  = new LinkedList<CalculateNode>();
    private int                  offset = 0;
    private String               el;
    private NodeFactory          nodeFactory;
    private int                  function;
    
    public static Lexer parse(String el)
    {
        return new Lexer(el);
    }
    
    public static Lexer parse(String el, int function)
    {
        return new Lexer(el, function);
    }
    
    private Lexer(String el)
    {
        nodeFactory = new BuildInNodeFactory();
        function = 0;
        this.el = el;
        scan();
    }
    
    private Lexer(String el, int function)
    {
        this.el = el;
        this.function = function;
        nodeFactory = new BuildInNodeFactory();
        scan();
    }
    
    private void scan()
    {
        while (true)
        {
            skipIgnoredToken();
            if (isEnd())
            {
                break;
            }
            else if (isIdentifierBegin())
            {
                scanIdentifier();
            }
            else if (isLeftParen())
            {
                scanLeftParen();
            }
            else if (isRightParen())
            {
                scanRightParen();
            }
            else if (isLeftBracket())
            {
                scanLeftBracket();
            }
            else if (isRightBracket())
            {
                scanRightBracket();
            }
            else if (isDot())
            {
                scanDot();
            }
            else if (isComma())
            {
                scanComma();
            }
            else if (isCharactersBegin())
            {
                scanCharacters();
            }
            else if (isMinusBegin())
            {
                if (nodes.peek() != null && nodes.peek().type() instanceof Operator == false)
                {
                    scanOperator();
                }
                else
                {
                    scanNumber();
                }
            }
            else if (isNumberBegin())
            {
                scanNumber();
            }
            else if (isOperatorBegin())
            {
                scanOperator();
            }
        }
        List<CalculateNode> list = new ArrayList<CalculateNode>();
        CalculateNode tmp;
        while ((tmp = nodes.pollFirst()) != null)
        {
            list.add(0, tmp);
        }
        parseNode = aggregate(list);
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
    
    private boolean isLeftBracket()
    {
        return '[' == getCurrentChar(0);
    }
    
    private boolean isRightBracket()
    {
        return ']' == getCurrentChar(0);
    }
    
    private void scanLeftBracket()
    {
        nodes.push(nodeFactory.parseSymBol(Symbol.LEFT_BRACKET, function));
        offset += 1;
    }
    
    private boolean isMinusBegin()
    {
        return '-' == getCurrentChar(0);
    }
    
    private boolean isOperatorBegin()
    {
        return CharType.isOperator(getCurrentChar(0));
    }
    
    private void scanOperator()
    {
        String literals = new String(new char[] { getCurrentChar(0), getCurrentChar(1) });
        if (Operator.literalsOf(literals) != null)
        {
            nodes.push(nodeFactory.buildOperatorNode(Operator.literalsOf(literals), function));
            offset += 2;
            return;
        }
        literals = String.valueOf(getCurrentChar(0));
        if (Operator.literalsOf(literals) != null)
        {
            nodes.push(nodeFactory.buildOperatorNode(Operator.literalsOf(literals), function));
            offset += 1;
            return;
        }
        throw new IllegalArgumentException(StringUtil.format("无法识别:{},{}", literals, el.substring(0, offset)));
    }
    
    private boolean isCharactersBegin()
    {
        return '\'' == getCurrentChar(0);
    }
    
    private void scanCharacters()
    {
        int length = 1;
        while (getCurrentChar(length) != '\'')
        {
            length += 1;
        }
        String literals = el.substring(offset + 1, offset + length);
        nodes.push(nodeFactory.buildStringNode(literals, function));
        offset += length + 1;
    }
    
    private boolean isComma()
    {
        return ',' == getCurrentChar(0);
    }
    
    private void scanComma()
    {
        nodes.push(nodeFactory.parseSymBol(Symbol.COMMA, function));
        offset += 1;
    }
    
    private void scanIdentifier()
    {
        nodes.push(parseIdentifier());
    }
    
    private boolean isRightParen()
    {
        return ')' == getCurrentChar(0);
    }
    
    private void scanRightBracket()
    {
        List<CalculateNode> list = new LinkedList<CalculateNode>();
        CalculateNode pred;
        while ((pred = nodes.pollFirst()) != null)
        {
            if (pred.type() != Symbol.LEFT_BRACKET)
            {
                list.add(0, pred);
            }
            else
            {
                break;
            }
        }
        if (pred == null)
        {
            throw new IllegalArgumentException(el.substring(0, offset));
        }
        CalculateNode valueNode = aggregate(list);
        CalculateNode beanNode = nodes.pollFirst();
        if (beanNode == null)
        {
            throw new UnsupportedOperationException();
        }
        nodes.push(nodeFactory.buildBracketNode(beanNode, valueNode));
        offset += 1;
    }
    
    private void scanRightParen()
    {
        List<CalculateNode> list = new LinkedList<CalculateNode>();
        CalculateNode pred;
        while ((pred = nodes.pollFirst()) != null)
        {
            if (pred.type() != Symbol.LEFT_PAREN && pred.type() != Expression.METHOD)
            {
                list.add(0, pred);
            }
            else
            {
                break;
            }
        }
        if (pred == null)
        {
            throw new IllegalArgumentException(el.substring(0, offset));
        }
        if (pred.type() == Expression.METHOD)
        {
            MethodNode methodNode = (MethodNode) pred;
            List<CalculateNode> argsNodes = new LinkedList<CalculateNode>();
            for (int i = 0; i < list.size();)
            {
                if (list.get(i).type() == Symbol.COMMA)
                {
                    list.remove(i);
                    argsNodes.add(aggregate(list.subList(0, i)));
                    list.remove(0);
                    i = 0;
                }
                else
                {
                    i++;
                }
            }
            if (list.isEmpty() == false)
            {
                argsNodes.add(aggregate(list));
            }
            methodNode.setArgsNodes(argsNodes.toArray(new CalculateNode[argsNodes.size()]));
            offset += 1;
            nodes.push(methodNode);
        }
        else
        {
            nodes.push(aggregate(list));
            offset += 1;
        }
    }
    
    private CalculateNode aggregate(List<CalculateNode> list)
    {
        // 只有一个参数的情况
        if (list.size() == 1)
        {
            return list.get(0);
        }
        // 只有一个操作符
        else if (list.size() == 3)
        {
            // 第二个node是操作符
            if (Operator.class.isAssignableFrom(list.get(1).type().getClass()))
            {
                return nodeFactory.buildOperatorResultNode(list.get(0), list.get(1), list.get(2), function);
            }
            // 否则是非法情况
            else
            {
                throw new IllegalArgumentException(el.substring(0, offset));
            }
        }
        // 具备至少2个操作符
        else if (list.size() >= 5)
        {
            // 优先操作乘法，除法，取余
            for (int i = 0; i < list.size();)
            {
                CalculateType type = list.get(i).type();
                if (type == Operator.MULTI || type == Operator.DIVISION || type == Operator.PERCENT)
                {
                    if (i > 0 && list.size() > i + 1//
                            && Operator.isOperator(list.get(i - 1).type()) == false//
                            && Operator.isOperator(list.get(i + 1).type()) == false//
                    )
                    {
                        CalculateNode resultNode = nodeFactory.buildOperatorResultNode(list.get(i - 1), list.get(i), list.get(i + 1), function);
                        list.remove(i - 1);
                        list.remove(i - 1);
                        list.remove(i - 1);
                        // 执行3次消除操作符和2个操作数
                        list.add(i - 1, resultNode);
                    }
                    else
                    {
                        throw new IllegalArgumentException(el.substring(0, offset));
                    }
                }
                else
                {
                    i++;
                }
            }
            for (int i = 0; i < list.size();)
            {
                CalculateType type = list.get(i).type();
                if (type == Operator.QUESTION)
                {
                    if (i == 0)
                    {
                        throw new IllegalArgumentException(StringUtil.format("?操作符前面应该有操作数,问题区间:{}", el.substring(0, offset)));
                    }
                    CalculateNode pred = list.get(i - 1);
                    // 删除前置节点
                    list.remove(i - 1);
                    // 删除？节点
                    list.remove(i - 1);
                    list.add(i - 1, nodeFactory.buildQuestionNode(pred));
                }
                else if (type == Operator.COLON)
                {
                    list.remove(i);
                    boolean find = false;
                    List<CalculateNode> tmp = new LinkedList<CalculateNode>();
                    for (int index = i - 1; index >= 0; index--)
                    {
                        if (list.get(index).type() != Expression.QUESTION)
                        {
                            tmp.add(0, list.get(index));
                            list.remove(index);
                        }
                        else
                        {
                            if (tmp.size() == 0)
                            {
                                throw new IllegalArgumentException("?和:之间缺少有效表达式,问题区间:" + el.substring(0, offset));
                            }
                            find = true;
                            CalculateNode leftNode = aggregate(tmp);
                            ((QuestionNode) list.get(index)).setLeftNode(leftNode);
                            i = index + 1;
                            break;
                        }
                    }
                    if (find == false)
                    {
                        throw new IllegalArgumentException("不是有效的三元表达式：" + el.substring(0, offset));
                    }
                    if (i >= list.size())
                    {
                        throw new IllegalArgumentException("不是有效的三元表达式：" + el.substring(0, offset));
                    }
                    ((QuestionNode) list.get(i - 1)).setRightNode(list.get(i));
                    list.remove(i);
                }
                else if (Operator.isOperator(type))
                {
                    if (i > 0 && list.size() > i + 1//
                            && Operator.isOperator(list.get(i - 1).type()) == false//
                            && Operator.isOperator(list.get(i + 1).type()) == false//
                    )
                    {
                        CalculateNode resultNode = nodeFactory.buildOperatorResultNode(list.get(i - 1), list.get(i), list.get(i + 1), function);
                        list.remove(i - 1);
                        list.remove(i - 1);
                        list.remove(i - 1);
                        // 执行3次消除操作符和2个操作数
                        list.add(i - 1, resultNode);
                    }
                    else
                    {
                        throw new IllegalArgumentException(el.substring(0, offset));
                    }
                }
                else
                {
                    i++;
                }
            }
            if (list.size() != 1)
            {
                throw new IllegalArgumentException(el.substring(0, offset));
            }
            return list.get(0);
        }
        else
        {
            throw new IllegalArgumentException(el.substring(0, offset));
        }
    }
    
    private boolean isLeftParen()
    {
        return '(' == getCurrentChar(0);
    }
    
    private void scanLeftParen()
    {
        offset += 1;
        nodes.push(nodeFactory.parseSymBol(Symbol.LEFT_PAREN, function));
    }
    
    private boolean isIdentifierBegin()
    {
        return CharType.isAlphabet(getCurrentChar(0));
    }
    
    private boolean isNumberBegin()
    {
        return CharType.isDigital(getCurrentChar(0)) //
                || ('-' == getCurrentChar(0) && CharType.isDigital(getCurrentChar(1)));
    }
    
    private void scanNumber()
    {
        char c = getCurrentChar(0);
        int length = 0;
        length += 1;
        boolean hasDot = false;
        while (CharType.isDigital(c = getCurrentChar(length)) || (hasDot == false && c == '.'))
        {
            length++;
            if (c == '.')
            {
                hasDot = true;
            }
        }
        if (c == '.')
        {
            throw new IllegalArgumentException(el.substring(offset, offset + length));
        }
        nodes.push(nodeFactory.buildNumberNode(el.substring(offset, offset + length), function));
        offset += length;
    }
    
    private boolean isEnd()
    {
        return offset >= el.length();
    }
    
    protected final char getCurrentChar(final int offset)
    {
        return this.offset + offset >= el.length() ? (char) CharType.EOI : el.charAt(this.offset + offset);
    }
    
    private void skipIgnoredToken()
    {
        int length = 0;
        while (CharType.isWhitespace(getCurrentChar(length)))
        {
            length++;
        }
        offset += length;
    }
    
    private boolean isDot()
    {
        return '.' == getCurrentChar(0);
    }
    
    private void scanDot()
    {
        offset += 1;
        int length = 0;
        char c;
        while (CharType.isAlphabet(c = getCurrentChar(length)) || CharType.isDigital(c))
        {
            length++;
        }
        if (c == '(')
        {
            String literals = el.substring(offset, offset + length);
            CalculateNode beanNode = nodes.pop();
            CalculateNode methodNode = nodeFactory.buildMethodNode(beanNode, literals, function);
            nodes.push(methodNode);
            offset += length + 1;
        }
        // 不是方法，则必然是属性
        else
        {
            String literals = el.substring(offset, offset + length);
            CalculateNode beanNode = nodes.pop();
            CalculateNode current = nodeFactory.buildPropertyNode(beanNode, literals, function);
            nodes.push(current);
            offset += length;
        }
    }
    
    private CalculateNode parseIdentifier()
    {
        int length = 0;
        char c;
        while (CharType.isAlphabet(c = getCurrentChar(length)) || CharType.isDigital(c))
        {
            length++;
        }
        String literals = el.substring(offset, offset + length);
        offset += length;
        if (DefaultKeyWord.getDefaultKeyWord(literals) != null)
        {
            return nodeFactory.parseKeyWord(literals, function);
        }
        else
        {
            return nodeFactory.parseVariable(literals, function);
        }
    }
    
}
