package com.jfireframework.jfireel.node;

import java.util.Map;

public class VariablePropertyNode extends PropertyNode
{
    private String variable;
    
    /**
     * 使用通过变量名和属性名访问该变量的属性
     * 
     * @param literals
     */
    public VariablePropertyNode(String literals)
    {
        int index = literals.indexOf('.');
        variable = literals.substring(0, index);
        propertyName = literals.substring(index + 1);
    }
    
    @Override
    public Object calculate(Map<String, Object> variables)
    {
        Object value = variables.get(variable);
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
