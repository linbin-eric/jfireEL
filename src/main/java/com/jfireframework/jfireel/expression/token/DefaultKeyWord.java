package com.jfireframework.jfireel.expression.token;

import java.util.HashMap;
import java.util.Map;

public enum DefaultKeyWord implements KeyWord
{
    TRUE, FALSE, NULL;
    
    private static Map<String, DefaultKeyWord> defaultKeeyWords = new HashMap<String, DefaultKeyWord>(128);
    
    static
    {
        for (DefaultKeyWord each : DefaultKeyWord.values())
        {
            defaultKeeyWords.put(each.name().toLowerCase(), each);
        }
    }
    
    public static DefaultKeyWord getDefaultKeyWord(String literals)
    {
        return defaultKeeyWords.get(literals.toLowerCase());
    }
}
