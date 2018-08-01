package com.jfireframework.jfireel.template.parser.impl;

import java.util.Deque;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.exception.IllegalFormatException;
import com.jfireframework.jfireel.expression.Expression;
import com.jfireframework.jfireel.template.ScanMode;
import com.jfireframework.jfireel.template.Template;
import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.execution.impl.ExpressionExecution;
import com.jfireframework.jfireel.template.parser.Invoker;
import com.jfireframework.jfireel.template.parser.Parser;

public class ExpressionParser extends Parser
{
    
    @Override
    public int parse(String sentence, int offset, Deque<Execution> executions, Template template, StringCache cache, Invoker next)
    {
        if (template.getMode() != ScanMode.LITERALS)
        {
            return next.scan(sentence, offset, executions, template, cache);
        }
        if (getChar(offset, sentence) != '$' || getChar(offset + 1, sentence) != '{')
        {
            return next.scan(sentence, offset, executions, template, cache);
        }
        extractLiterals(cache, executions);
        offset += 2;
        int start = offset;
        int length = sentence.length();
        while (getChar(offset, sentence) != '}' && offset < length)
        {
            offset++;
        }
        if (offset >= length)
        {
            throw new IllegalFormatException("语法错误，不是闭合的表达式", sentence.substring(0, start));
        }
        ExpressionExecution execution = new ExpressionExecution(Expression.parse(sentence.substring(start, offset)));
        executions.push(execution);
        return offset + 1;
    }
    
}
