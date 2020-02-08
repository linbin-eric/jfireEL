package com.jfireframework.jfireel.template.parser.impl;

import java.util.Deque;
import com.jfireframework.jfireel.template.ScanMode;
import com.jfireframework.jfireel.template.Template;
import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.parser.Invoker;
import com.jfireframework.jfireel.template.parser.Parser;

public class ExecutionBeginParser extends Parser
{
    
    @Override
    public int parse(String sentence, int offset, Deque<Execution> executions, Template template, StringBuilder cache, Invoker next)
    {
        if (isExecutionBegin(offset, sentence) == false)
        {
            return next.scan(sentence, offset, executions, template, cache);
        }
        offset += 2;
        template.setMode(ScanMode.EXECUTION);
        extractLiterals(cache, executions);
        offset = skipWhiteSpace(offset, sentence);
        return offset;
    }
    
}
