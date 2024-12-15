package com.jfirer.jfireel.expression.parse.impl;

import com.jfirer.jfireel.expression.CharType;
import com.jfirer.jfireel.expression.Matrix;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.CompileStaticCallOperand;
import com.jfirer.jfireel.expression.impl.operand.FunctionCallOperand;
import com.jfirer.jfireel.expression.impl.operand.InnerCallOperand;
import com.jfirer.jfireel.expression.impl.operator.SpotOperator;
import com.jfirer.jfireel.expression.parse.TokenParser;
import lombok.SneakyThrows;

public class InnerCallOrFunctionCallParser implements TokenParser
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
            if (matrix.findInnerCall(el.substring(parseContext.getIndex(), index)) != null && parseContext.getOperatorStack().peek() instanceof SpotOperator == false)
            {
                InnerCallOperand innerCallOperand = new InnerCallOperand(matrix.findInnerCall(el.substring(parseContext.getIndex(), index)));
                parseContext.getOperandStack().push(innerCallOperand);
                parseContext.getRecognizeToken().add(innerCallOperand);
                parseContext.setIndex(index);
                return true;
            }
            else if (matrix.findFunctionCall(el.substring(parseContext.getIndex(), index)) != null && parseContext.getOperatorStack().peek() instanceof SpotOperator == false)
            {
                FunctionCallOperand.FunctionCallData callData = matrix.findFunctionCall(el.substring(parseContext.getIndex(), index));
                FunctionCallOperand                  functionCallOperand = new FunctionCallOperand(callData.getParamNames(), callData.getFunction());
                parseContext.getOperandStack().push(functionCallOperand);
                parseContext.getRecognizeToken().add(functionCallOperand);
                parseContext.setIndex(index);
                return true;
            }
            else if (matrix.findMethodHandle(el.substring(parseContext.getIndex(), index)) != null && parseContext.getOperatorStack().peek() instanceof SpotOperator == false)
            {
                Class<? extends CompileStaticCallOperand> ckass   = matrix.findMethodHandle(el.substring(parseContext.getIndex(), index));
                CompileStaticCallOperand                  operand = ckass.getConstructor().newInstance();
                parseContext.getOperandStack().push(operand);
                parseContext.getRecognizeToken().add(operand);
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
