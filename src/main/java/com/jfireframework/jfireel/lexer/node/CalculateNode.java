package com.jfireframework.jfireel.lexer.node;

import java.util.Map;
import com.jfireframework.jfireel.lexer.token.CalculateType;

public interface CalculateNode
{
    
    Object calculate(Map<String, Object> variables);
    
    CalculateType type();
}
