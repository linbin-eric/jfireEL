package com.jfirer.jfireel.expression.node.impl;

import java.lang.reflect.Field;
import java.util.Map;

import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.token.Token;
import com.jfirer.jfireel.expression.token.TokenType;
import com.jfirer.jfireel.expression.token.ValueResult;

public class ClassPropertyNode implements CalculateNode
{
    protected final Class<?> beanType;
    protected final Field    field;

    /**
     * 使用通过变量名和属性名访问该变量的属性
     *
     * @param literals
     */
    public ClassPropertyNode(String literals, CalculateNode beanNode)
    {
        try
        {
            beanType = (Class<?>) beanNode.calculate(null);
            field = beanType.getField(literals);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("类型的静态属性无法获取到,检查" + literals, e);
        }
    }

    @Override
    public Object calculate(Map<String, Object> variables)
    {
        try
        {
            return field.get(null);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TokenType type()
    {
        return TokenType.PROPERTY;
    }

    @Override
    public Token token()
    {
        return ValueResult.RESULT;
    }

    @Override
    public String toString()
    {
        return literals();
    }

    @Override
    public String literals()
    {
        return beanType.getName() + "." + field.getName();
    }
}
