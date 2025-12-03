package cc.jfire.el.expression.parse.impl;

import cc.jfire.el.expression.CharType;
import cc.jfire.el.expression.Matrix;
import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operand.ClassOperand;
import cc.jfire.el.expression.impl.operator.SpotOperator;
import cc.jfire.el.expression.parse.TokenParser;

public class StaticClassParser implements TokenParser
{
    @Override
    public boolean parse(ParseContext parseContext)
    {
        int    index  = parseContext.getIndex();
        String el     = parseContext.getEl();
        Matrix matrix = parseContext.getMatrix();
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
                ClassOperand classOperand = new ClassOperand(Class.forName(className));
                parseContext.getOperandStack().push(classOperand);
                parseContext.getRecognizeToken().add(classOperand);
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
            if (matrix.findClassByName(el.substring(parseContext.getIndex(), index)) != null && parseContext.getOperatorStack().peek() instanceof SpotOperator == false)
            {
                ClassOperand classOperand = new ClassOperand(matrix.findClassByName(el.substring(parseContext.getIndex(), index)));
                parseContext.getOperandStack().push(classOperand);
                parseContext.setIndex(index);
                parseContext.getRecognizeToken().add(classOperand);
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
