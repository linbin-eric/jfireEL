package com.jfirer.jfireel.expression2.parse.impl;

import com.jfirer.jfireel.expression.util.CharType;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.MethodInvokeOperand;
import com.jfirer.jfireel.expression2.impl.operator.SpotOperator;
import com.jfirer.jfireel.expression2.parse.TokenParser;

import java.util.Set;

public class DirectMethodParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        int         index         = parseContext.getIndex();
        String      el            = parseContext.getEl();
        Set<String> methodNameSet = parseContext.getDirectMethods().keySet();
        if (CharType.isAlphabet(el.charAt(index)))
        {
            index += 1;
            while (index < el.length() && (CharType.isAlphabet(el.charAt(index)) || CharType.isDigital(el.charAt(index))))
            {
                index += 1;
            }
            int mark = index;
            while (index < el.length())
            {
                char c = el.charAt(index);
                if (CharType.isIgnore(c))
                {
                    index += 1;
                }
                else if (c == '(')
                {
                    break;
                }
                else
                {
                    return false;
                }
            }
            index = mark;
            if (methodNameSet.contains(el.substring(parseContext.getIndex(), index)) && parseContext.getOperatorStack().peek() instanceof SpotOperator == false)
            {
                String methodName = el.substring(parseContext.getIndex(), index);
                parseContext.getOperandStack().push(new MethodInvokeOperand.UnFinishDirectMethod(parseContext.getDirectMethods().get(methodName), methodName, null, false, el.substring(0, index)));
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
