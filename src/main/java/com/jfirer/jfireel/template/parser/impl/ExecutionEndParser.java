package com.jfirer.jfireel.template.parser.impl;

import com.jfirer.jfireel.template.ScanMode;
import com.jfirer.jfireel.template.Template;
import com.jfirer.jfireel.template.execution.Execution;
import com.jfirer.jfireel.template.parser.Invoker;
import com.jfirer.jfireel.template.parser.Parser;

import java.util.Deque;

public class ExecutionEndParser extends Parser
{

    @Override
    public int parse(String sentence, int offset, Deque<Execution> executions, Template template, StringBuilder cache, Invoker next)
    {
        if (template.getMode() != ScanMode.EXECUTION //
                || '%' != getChar(offset, sentence) //
                || '>' != getChar(offset + 1, sentence))
        {
            return next.scan(sentence, offset, executions, template, cache);
        }
        template.setMode(ScanMode.LITERALS);
        offset += 2;
        return offset;
    }
}
