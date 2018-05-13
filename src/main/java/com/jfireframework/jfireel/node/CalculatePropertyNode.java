package com.jfireframework.jfireel.node;

import java.util.Map;

public class CalculatePropertyNode extends PropertyNode
{
    private CalculateNode beanNode;
    
    /**
     * 使用通过变量名和属性名访问该变量的属性
     * 
     * @param literals
     */
    public CalculatePropertyNode(String literals, CalculateNode beanNode)
    {
        propertyName = literals;
        this.beanNode = beanNode;
    }
    
    @Override
    public Object calculate(Map<String, Object> variables)
    {
        Object value = beanNode.calculate(variables);
        if (value == null)
        {
            return null;
        }
        try
        {
            return getField(value).get(value);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
