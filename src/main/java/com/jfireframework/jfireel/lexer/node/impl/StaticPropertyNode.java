package com.jfireframework.jfireel.lexer.node.impl;

import java.lang.reflect.Field;
import java.util.Map;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;

public class StaticPropertyNode implements CalculateNode
{
    protected final Class<?> beanType;
    protected final Field    field;
    
    /**
     * 使用通过变量名和属性名访问该变量的属性
     * 
     * @param literals
     */
    public StaticPropertyNode(String literals, CalculateNode beanNode)
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
    public CalculateType type()
    {
        return Expression.PROPERTY;
    }
    
    @Override
    public String toString()
    {
        return "StaticPropertyNode [beanType=" + beanType + ", field=" + field + "]";
    }
    
}
