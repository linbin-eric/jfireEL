package com.jfirer.jfireel.template.parser.impl;

import com.jfirer.jfireel.exception.IllegalFormatException;
import com.jfirer.jfireel.template.ScanMode;
import com.jfirer.jfireel.template.Template;
import com.jfirer.jfireel.template.execution.Execution;
import com.jfirer.jfireel.template.execution.WithBodyExecution;
import com.jfirer.jfireel.template.execution.impl.ElseExecution;
import com.jfirer.jfireel.template.execution.impl.ElseIfExecution;
import com.jfirer.jfireel.template.execution.impl.IfExecution;
import com.jfirer.jfireel.template.parser.Invoker;
import com.jfirer.jfireel.template.parser.Parser;

import java.util.Deque;
import java.util.LinkedList;

public class EndBraceParser extends Parser
{

    @Override
    public int parse(String sentence, int offset, Deque<Execution> executions, Template template, StringBuilder cache, Invoker next)
    {
        if (template.getMode() != ScanMode.EXECUTION || getChar(offset, sentence) != '}')
        {
            return next.scan(sentence, offset, executions, template, cache);
        }
        Deque<Execution> array = new LinkedList<Execution>();
        Execution        pop;
        while ((pop = executions.pollFirst()) != null)
        {
            if (pop instanceof WithBodyExecution == false || ((WithBodyExecution) pop).isBodyNotSet() == false)
            {
                array.push(pop);
            }
            else
            {
                break;
            }
        }
        if (pop == null)
        {
            throw new IllegalFormatException("结束符}前面没有开始符号", sentence.substring(0, offset));
        }
        ((WithBodyExecution) pop).setBody(array.toArray(emptyBody));
        if (pop instanceof ElseExecution)
        {
            if (executions.peek() == null || executions.peek() instanceof IfExecution == false)
            {
                throw new IllegalFormatException("else 节点之前没有if节点", sentence.substring(0, offset));
            }
            ((IfExecution) executions.peek()).setElse((ElseExecution) pop);
        }
        else if (pop instanceof ElseIfExecution)
        {
            if (executions.peek() == null || executions.peek() instanceof IfExecution == false)
            {
                throw new IllegalFormatException("else if 节点之前没有if节点", sentence.substring(0, offset));
            }
            ((IfExecution) executions.peek()).addElseIf((ElseIfExecution) pop);
        }
        else
        {
            executions.push(pop);
        }
        offset += 1;
        return offset;
    }
}
