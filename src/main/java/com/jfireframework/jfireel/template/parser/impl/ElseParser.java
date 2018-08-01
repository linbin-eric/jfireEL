package com.jfireframework.jfireel.template.parser.impl;

import java.util.Deque;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.exception.IllegalFormatException;
import com.jfireframework.jfireel.expression.Expression;
import com.jfireframework.jfireel.template.ScanMode;
import com.jfireframework.jfireel.template.Template;
import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.execution.impl.ElseExecution;
import com.jfireframework.jfireel.template.execution.impl.ElseIfExecution;
import com.jfireframework.jfireel.template.parser.Invoker;
import com.jfireframework.jfireel.template.parser.Parser;

public class ElseParser extends Parser
{
    
    @Override
    public int parse(String sentence, int offset, Deque<Execution> executions, Template template, StringCache cache, Invoker next)
    {
        if (template.getMode() != ScanMode.EXECUTION)
        {
            return next.scan(sentence, offset, executions, template, cache);
        }
        if (getChar(offset, sentence) != 'e'//
                || getChar(offset + 1, sentence) != 'l' //
                || getChar(offset + 2, sentence) != 's'//
                || getChar(offset + 3, sentence) != 'e'//
        )
        {
            return next.scan(sentence, offset, executions, template, cache);
        }
        offset = skipWhiteSpace(offset + 4, sentence);
        // 此种情况意味着是一个单纯的else
        if (getChar(offset, sentence) == '{')
        {
            ElseExecution execution = new ElseExecution();
            executions.push(execution);
            offset++;
            return offset;
        }
        else if (getChar(offset, sentence) == 'i' && getChar(offset + 1, sentence) == 'f')
        {
            offset = skipWhiteSpace(offset + 2, sentence);
            if (getChar(offset, sentence) != '(')
            {
                throw new IllegalFormatException("else if的条件没有以(开始", sentence.substring(0, offset));
            }
            int leftBracketIndex = offset;
            offset = findEndRightBracket(sentence, offset);
            if (offset == -1)
            {
                throw new IllegalFormatException("else if的条件没有以)结束", sentence.substring(0, leftBracketIndex));
            }
            ElseIfExecution execution = new ElseIfExecution(Expression.parse(sentence.substring(leftBracketIndex + 1, offset)));
            executions.push(execution);
            offset++;
            offset = findMethodBodyBegin(sentence, offset);
            return offset;
        }
        else
        {
            throw new IllegalFormatException("无法识别的语法内容", sentence.substring(0, offset));
        }
    }
    
}
