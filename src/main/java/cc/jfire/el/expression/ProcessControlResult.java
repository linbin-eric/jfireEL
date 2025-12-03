package cc.jfire.el.expression;

import lombok.Data;

@Data
public class ProcessControlResult
{
    private final ControlFlag flag;
    private final Object      resultValue;
}
