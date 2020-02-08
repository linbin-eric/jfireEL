package com.jfireframework.jfireel.template.parser.impl;

import java.util.Deque;
import com.jfireframework.jfireel.exception.IllegalFormatException;
import com.jfireframework.jfireel.expression.Expression;
import com.jfireframework.jfireel.template.ScanMode;
import com.jfireframework.jfireel.template.Template;
import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.execution.impl.IfExecution;
import com.jfireframework.jfireel.template.parser.Invoker;
import com.jfireframework.jfireel.template.parser.Parser;

public class IfParser extends Parser
{
    
    @Override
    public int parse(String sentence, int offset, Deque<Execution> executions, Template template, StringBuilder cache, Invoker next)
    {
        if (template.getMode() != ScanMode.EXECUTION)
        {
            return next.scan(sentence, offset, executions, template, cache);
        }
        int origin = offset;
        offset = skipWhiteSpace(offset, sentence);
        if (getChar(offset, sentence) != 'i' || getChar(offset + 1, sentence) != 'f')
        {
            return next.scan(sentence, origin, executions, template, cache);
        }
        offset = skipWhiteSpace(offset + 2, sentence);
        if ('(' != getChar(offset, sentence))
        {
            throw new IllegalFormatException("IF条件没有以(开始进行包围", sentence.substring(0, offset));
        }
        int leftBracketIndex = offset;
        offset = findEndRightBracket(sentence, offset);
        if (offset == -1)
        {
            throw new IllegalFormatException("if条件没有用)包围", sentence.substring(0, leftBracketIndex));
        }
        String ifLiterals = sentence.substring(leftBracketIndex + 1, offset);
        Expression expression = Expression.parse(ifLiterals);
        IfExecution execution = new IfExecution(expression);
        executions.push(execution);
        offset++;
        offset = findMethodBodyBegin(sentence, offset);
        return offset;
    }
    
}
