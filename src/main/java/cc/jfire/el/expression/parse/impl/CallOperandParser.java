package cc.jfire.el.expression.parse.impl;

import cc.jfire.el.expression.CharType;
import cc.jfire.el.expression.Matrix;
import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operand.CallOperandPlaceHolder;
import cc.jfire.el.expression.impl.operator.SpotOperator;
import cc.jfire.el.expression.parse.TokenParser;
import lombok.SneakyThrows;

public class CallOperandParser implements TokenParser
{
    @SneakyThrows
    @Override
    public boolean parse(ParseContext parseContext)
    {
        int    index  = parseContext.getIndex();
        String el     = parseContext.getEl();
        Matrix matrix = parseContext.getMatrix();
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
            if (matrix.existCallOperand(el.substring(parseContext.getIndex(), index)) && parseContext.getOperatorStack().peek() instanceof SpotOperator == false)
            {
                CallOperandPlaceHolder placeHolder = new CallOperandPlaceHolder().setName(el.substring(parseContext.getIndex(), index)).setMatrix(matrix);
                parseContext.getOperandStack().push(placeHolder);
                parseContext.getRecognizeToken().add(placeHolder);
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
