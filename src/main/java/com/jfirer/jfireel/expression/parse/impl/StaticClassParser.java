package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.CharType;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.TokenType;
import com.jfirer.jfireel.expression.impl.operand.ClassOperand;
import com.jfirer.jfireel.expression.impl.operator.SpotOperator;
import com.jfirer.jfireel.expression.parse.TokenParser;

import java.util.Map;

public class StaticClassParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        int                   index           = parseContext.getIndex();
        String                el              = parseContext.getEl();
        Map<String, Class<?>> staticClassName = parseContext.getClassName();
        if (el.charAt(index) == '@' && index + 1 < el.length() && el.charAt(index + 1) == '(')
        {
            index += 2;
            while (index < el.length() && (CharType.isAlphabet(el.charAt(index)) || CharType.isDigital(el.charAt(index)) || el.charAt(index) == '.'))
            {
                index += 1;
            }
            if (el.charAt(index) != ')')
            {
                return false;
            }
            String className = el.substring(parseContext.getIndex() + 2, index);
            try
            {
                parseContext.getOperandStack().push(new ClassOperand(Class.forName(className)));
                parseContext.setLastToken(TokenType.OPERAND);
            }
            catch (ClassNotFoundException e)
            {
                throw new IllegalStateException("解析表达式异常，无法加载静态类：" + className + ",解析位置在" + el.substring(0, index));
            }
            parseContext.setIndex(index + 1);
            return true;
        }
        else if (CharType.isAlphabet(el.charAt(index)))
        {
            index += 1;
            while (index < el.length() && (CharType.isAlphabet(el.charAt(index)) || CharType.isDigital(el.charAt(index))))
            {
                index += 1;
            }
            if (staticClassName.containsKey(el.substring(parseContext.getIndex(), index)) && parseContext.getOperatorStack().peek() instanceof SpotOperator == false)
            {
                parseContext.getOperandStack().push(new ClassOperand(staticClassName.get(el.substring(parseContext.getIndex(), index))));
                parseContext.setIndex(index);
                parseContext.setLastToken(TokenType.OPERAND);
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
