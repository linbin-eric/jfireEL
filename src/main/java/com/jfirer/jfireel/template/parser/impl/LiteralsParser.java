package com.jfirer.jfireel.template.parser.impl;

import com.jfirer.jfireel.template.ScanMode;
import com.jfirer.jfireel.template.Template;
import com.jfirer.jfireel.template.execution.Execution;
import com.jfirer.jfireel.template.parser.Invoker;
import com.jfirer.jfireel.template.parser.Parser;

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
