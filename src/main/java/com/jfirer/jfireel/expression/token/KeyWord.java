package com.jfirer.jfireel.expression.token;

import java.util.HashMap;
import java.util.Map;

public enum KeyWord implements Token
{
    TRUE, FALSE, NULL;

    private static final Map<String, KeyWord> defaultKeeyWords = new HashMap<String, KeyWord>(128);

    static
    {
        for (KeyWord each : KeyWord.values())
        {
            defaultKeeyWords.put(each.name().toLowerCase(), each);
        }
    }

    public static KeyWord getKeyWord(String literals)
    {
        return defaultKeeyWords.get(literals.toLowerCase());
    }
}
