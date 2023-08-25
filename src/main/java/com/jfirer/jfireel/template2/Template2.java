package com.jfirer.jfireel.template2;

import com.jfirer.jfireel.expression2.Expression2;
import com.jfirer.jfireel.expression2.Operand;

import java.lang.reflect.Method;
import java.util.Map;

public class Template2
{
    public static final void print(Object value, StringBuilder builder)
    {
        builder.append(value);
    }

    static
    {
        try
        {
            Method print = Template2.class.getDeclaredMethod("print", Object.class, StringBuilder.class);
            Expression2.registerMethod(print);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static final int     IN_CODE_AREA = 1;
    private static final int     IN_TEXT      = 2;
    private static final int     IN_VARIABLE  = 3;
    private final        Operand operand;

    private Template2(Operand operand)
    {
        this.operand = operand;
    }

    public static Template2 parse(String content)
    {
        StringBuilder builder = new StringBuilder();
        int           type    = IN_TEXT;
        int           length  = content.length();
        int           index   = 0;
        int           mark    = 0;
        while (index < length)
        {
            char c = content.charAt(index);
            switch (type)
            {
                case IN_CODE_AREA ->
                {
                    if (c == '%' && index + 1 < length && content.charAt(index + 1) == '>')
                    {
                        builder.append(content.substring(mark, index));
                        mark = index += 2;
                        type = IN_TEXT;
                    }
                    else
                    {
                        index += 1;
                    }
                }
                case IN_TEXT ->
                {
                    if (c == '$' && index + 1 < length && content.charAt(index + 1) == '{')
                    {
                        if (mark != index)
                        {
                            builder.append("print('").append(content.substring(mark, index)).append("',outputStr);\r\n");
                        }
                        mark = index += 2;
                        type = IN_VARIABLE;
                    }
                    else if (c == '<' && index + 1 < length && content.charAt(index + 1) == '%')
                    {
                        if (mark != index)
                        {
                            builder.append("print('").append(content.substring(mark, index)).append("',outputStr);\r\n");
                        }
                        mark = index += 2;
                        type = IN_CODE_AREA;
                    }
                    else
                    {
                        index += 1;
                    }
                }
                case IN_VARIABLE ->
                {
                    if (c == '}')
                    {
                        builder.append("print(").append(content.substring(mark, index)).append(",outputStr);\r\n");
                        mark = index += 1;
                        type = IN_TEXT;
                    }
                    else
                    {
                        index += 1;
                    }
                }
            }
        }
        if (type != IN_TEXT)
        {
            throw new IllegalStateException("解析模板不正确，模板没有被正确结束");
        }
        if (mark != index)
        {
            builder.append("print('").append(content.substring(mark, index)).append("',outputStr);\r\n");
        }
        return new Template2(Expression2.parseMutli(builder.toString()));
    }

    public String render(Map<String, Object> params)
    {
        StringBuilder stringBuilder = new StringBuilder();
        params.put("outputStr", stringBuilder);
        operand.calculate(params);
        return stringBuilder.toString();
    }
}
