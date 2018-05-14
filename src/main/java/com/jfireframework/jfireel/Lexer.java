package com.jfireframework.jfireel;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import com.jfireframework.jfireel.node.CalculateNode;
import com.jfireframework.jfireel.node.CalculatePropertyNode;
import com.jfireframework.jfireel.node.ConstantNode;
import com.jfireframework.jfireel.node.MethodNode;
import com.jfireframework.jfireel.node.SymBolNode;
import com.jfireframework.jfireel.node.VariableMethodNode;
import com.jfireframework.jfireel.node.VariableNode;
import com.jfireframework.jfireel.node.VariablePropertyNode;

public class Lexer
{
    private Deque<CalculateNode>  nodes    = new LinkedList<CalculateNode>();
    private Deque<CalculateNode>  tmpStack = new LinkedList<CalculateNode>();
    private int                   offset   = 0;
    private String                el;
    private Map<String, Class<?>> variableTypes;
    
    public Lexer(String el, Map<String, Class<?>> variableTypes)
    {
        this.el = el;
        this.variableTypes = variableTypes;
    }
    
    public void scan()
    {
        while (true)
        {
            skipIgnoredToken();
            if (isEnd())
            {
                return;
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
            else if (isDot())
            {
                scanDot();
            }
        }
    }
    
    private void scanIdentifier()
    {
        nodes.push(parseIdentifier());
    }
    
    private boolean isRightParen()
    {
        return ')' == getCurrentChar(0);
    }
    
    private void scanRightParen()
    {
        CalculateNode pred;
        while ((pred = nodes.peek()).type() != Symbol.LEFT_PAREN || pred.type() != Expression.METHOD)
        {
            tmpStack.push(pred);
            nodes.pop();
        }
        if (pred.type() == Expression.METHOD)
        {
            CalculateNode paramNode;
            while ((paramNode = tmpStack.pop()) != null)
            {
                ((MethodNode) pred).addParamNode(paramNode);
            }
            offset += 1;
        }
        else
        {
            if (tmpStack.size() != 1)
            {
                throw new IllegalArgumentException(el.substring(0, offset + 1));
            }
            nodes.pop();
            nodes.push(tmpStack.pop());
            offset += 1;
        }
    }
    
    private boolean isLeftParen()
    {
        return '(' == getCurrentChar(0);
    }
    
    private void scanLeftParen()
    {
        offset += 1;
        nodes.push(new SymBolNode(Symbol.LEFT_PAREN));
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
    
    private boolean isSymbolBegin()
    {
        return CharType.isSymbol(getCurrentChar(0));
    }
    
    private boolean isCharsBegin()
    {
        return '\'' == getCurrentChar(0) || '\"' == getCurrentChar(0);
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
        int length = 0;
        char c;
        while (CharType.isAlphabet(c = getCurrentChar(length)) || CharType.isDigital(c))
        {
            length++;
        }
        if (c == '(')
        {
            
        }
        // 不是方法，则必然是属性
        else
        {
            String literals = el.substring(offset, offset + length);
            offset += length;
            CalculateNode pred = nodes.pop();
            CalculatePropertyNode current = new CalculatePropertyNode(literals, pred);
            nodes.push(current);
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
        if (c == '.')
        {
            while (CharType.isAlphabet(c = getCurrentChar(length)) || CharType.isDigital(c))
            {
                length++;
            }
            String literals = el.substring(offset, offset + length);
            // 这是一个方法调用
            if (c == '(')
            {
                // 略过第一个(
                offset += length + 1;
                return new VariableMethodNode(literals);
            }
            // 这是一个属性调用
            else
            {
                offset += length;
                return new VariablePropertyNode(literals);
            }
        }
        else
        {
            offset += length;
            String literals = el.substring(offset, offset + length);
            if (DefaultKeyWord.getDefaultKeyWord(literals) != null)
            {
                return new ConstantNode(literals);
            }
            else
            {
                return new VariableNode(literals);
            }
        }
    }
    
}
