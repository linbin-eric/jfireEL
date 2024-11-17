package com.jfirer.jfireel.template;

import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.Operand;

import java.util.Map;

public class Template
{
    static
    {
        Expression.registerInnerCall("out", (obj, array, map) -> {
            StringBuilder out = (StringBuilder) map.get("outputStr");
            out.append(array[0].calculate(map));
            return null;
        });
    }

    private static final int     IN_CODE_AREA = 1;
    private static final int     IN_TEXT      = 2;
    private static final int     IN_VARIABLE  = 3;
    private final        Operand operand;

    private Template(Operand operand)
    {
        this.operand = operand;
    }

    public static Template parse(String content)
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
                            builder.append("out('").append(content.substring(mark, index)).append("');\r\n");
                        }
                        mark = index += 2;
                        type = IN_VARIABLE;
                    }
                    else if (c == '<' && index + 1 < length && content.charAt(index + 1) == '%')
                    {
                        if (mark != index)
                        {
                            builder.append("out('").append(content.substring(mark, index)).append("');\r\n");
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
                        builder.append("out(").append(content.substring(mark, index)).append(");\r\n");
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
            builder.append("out('").append(content.substring(mark, index)).append("');\r\n");
        }
        return new Template(Expression.parse(builder.toString()));
    }

    public String render(Map<String, Object> params)
    {
        StringBuilder stringBuilder = new StringBuilder();
        params.put("outputStr", stringBuilder);
        operand.calculate(params);
        return stringBuilder.toString();
    }

    public void render(Map<String, Object> params, StringBuilder builder)
    {
        params.put("outputStr", builder);
        operand.calculate(params);
    }
}
