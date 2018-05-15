package com.jfireframework.jfireel.node;

import java.util.Map;
import com.jfireframework.jfireel.token.CalculateType;

public interface CalculateNode
{
    
    Object calculate(Map<String, Object> variables);
    
    CalculateType type();
}
