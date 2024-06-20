package com.jfirer.jfireel.expression;

public enum ControlFlag
{
    RETURN, BREAK, CONTINUE,
    /**
     * 在基于 EL 的 DSL 多语言执行中，最终 DSL 被解析为多个顺序执行的操作数。每一个操作数内部可能有若干执行语句。
     * 当某一个执行语句有 return 操作时，从整体的 DSL 来看，此时就已经可以结束整体运行，并且将这个 return 操作返还给最外层调用者。
     * 此时采用 RETURN_WITH_VALUE 标识符来包装这个返回值。这样，非最外层的复合操作数就可以知道需要将这个结果继续向上层返回，而不需要执行内部的后续操作数。
     */
    RETURN_WITH_VALUE;
}
