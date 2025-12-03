package cc.jfire.el.expression.format;

import lombok.Data;

@Data
public class FormatContext
{
    private int     indent = 0;
    private boolean first  = true;
}
