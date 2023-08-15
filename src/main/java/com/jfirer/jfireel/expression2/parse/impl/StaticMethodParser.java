package com.jfirer.jfireel.expression2.parse.impl;

import com.jfirer.jfireel.expression.util.CharType;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.CandidateStaticMethodOperand;
import com.jfirer.jfireel.expression2.impl.operand.StaticClassOperand;
import com.jfirer.jfireel.expression2.impl.operator.SpotOperator;
import com.jfirer.jfireel.expression2.parse.TokenParser;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class StaticMethodParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        int                   index           = parseContext.getIndex();
        String                el              = parseContext.getEl();
        Map<String, List<Method>> staticMethodName = parseContext.getStaticMethodName();
        if (CharType.isAlphabet(el.charAt(index)))
        {
            index += 1;
            while (CharType.isAlphabet(el.charAt(index)) || CharType.isDigital(el.charAt(index)))
            {
                index += 1;
            }
            if (staticMethodName.containsKey(el.substring(parseContext.getIndex(), index)) && parseContext.getOperatorStack().peek() instanceof SpotOperator ==false)
            {
                parseContext.getOperandStack().push(new CandidateStaticMethodOperand(staticMethodName.get(el.substring(parseContext.getIndex(), index))));
                parseContext.setIndex(index);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
