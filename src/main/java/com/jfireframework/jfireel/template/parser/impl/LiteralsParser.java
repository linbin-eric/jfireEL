package com.jfireframework.jfireel.template.parser.impl;

import com.jfireframework.jfireel.template.ScanMode;
import com.jfireframework.jfireel.template.Template;
import com.jfireframework.jfireel.template.execution.Execution;
import com.jfireframework.jfireel.template.parser.Invoker;
import com.jfireframework.jfireel.template.parser.Parser;

import java.util.Deque;

public class LiteralsParser extends Parser
{
    
    @Override
    public int parse(String sentence, int offset, Deque<Execution> executions, Template template, StringBuilder cache, Invoker next)
    {
        if (template.getMode() != ScanMode.LITERALS)
        {
            offset = skipWhiteSpace(offset, sentence);
            return offset;
        }
        cache.append(getChar(offset, sentence));
        return offset + 1;
    }
    
}
