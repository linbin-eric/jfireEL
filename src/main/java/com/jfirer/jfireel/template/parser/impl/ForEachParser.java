package com.jfirer.jfireel.template.parser.impl;

import com.jfirer.jfireel.exception.IllegalFormatException;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.util.CharType;
import com.jfirer.jfireel.template.ScanMode;
import com.jfirer.jfireel.template.Template;
import com.jfirer.jfireel.template.execution.Execution;
import com.jfirer.jfireel.template.execution.impl.ForEachExecution;
import com.jfirer.jfireel.template.parser.Invoker;
import com.jfirer.jfireel.template.parser.Parser;

import java.util.Deque;

public class ForEachParser extends Parser
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
        if (getChar(offset, sentence) != 'f'//
                || getChar(offset + 1, sentence) != 'o'//
                || getChar(offset + 2, sentence) != 'r'//
        )
        {
            return next.scan(sentence, origin, executions, template, cache);
        }
        offset = skipWhiteSpace(offset + 3, sentence);
        if (getChar(offset, sentence) != '(')
        {
            throw new IllegalFormatException("for循环没有以(开始条件语句", sentence.substring(0, offset));
        }
        offset = skipWhiteSpace(offset + 1, sentence);
        int start = offset;
        while (CharType.isAlphabet(getChar(offset, sentence)) && offset < sentence.length())
        {
            offset++;
        }
        if (offset >= sentence.length())
        {
            throw new IllegalFormatException("for循环中的变量命名没有结束", sentence.substring(0, start));
        }
        String itemName = sentence.substring(start, offset);
        if (getChar(offset, sentence) != ' ')
        {
            throw new IllegalFormatException("for循环语法错误", sentence.substring(0, start));
        }
        offset = skipWhiteSpace(offset + 1, sentence);
        if (getChar(offset, sentence) != 'i' || getChar(offset + 1, sentence) != 'n')
        {
            throw new IllegalFormatException("for循环语法错误,缺少IN", sentence.substring(0, start));
        }
        offset = skipWhiteSpace(offset + 2, sentence);
        start = offset;
        offset = findEndRightBracket(sentence, offset);
        if (offset == -1)
        {
            throw new IllegalFormatException("for循环语法错误,缺少集合", sentence.substring(0, start));
        }
        ForEachExecution execution = new ForEachExecution(itemName, Expression.parse(sentence.substring(start, offset)));
        executions.push(execution);
        offset++;
        offset = findMethodBodyBegin(sentence, offset);
        return offset;
    }
}
