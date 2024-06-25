package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.PlaceHolder;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.TokenType;
import com.jfirer.jfireel.expression.impl.operand.*;
import lombok.Data;

import java.util.Deque;

@Data
public class LeftAngleBracketOperator implements Operator
{
    private final String fragment;

    @Override
    public int priority()
    {
        return -1;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        parseContext.getOperatorStack().push(this);
        parseContext.getOperandStack().push(PlaceHolder.LEFT_ANGLE_BRACKET);
        parseContext.setLastToken(TokenType.OPERATOR);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Deque<Operand> processStack = parseContext.getProcessStack();
        Deque<Operand> operandStack = parseContext.getOperandStack();
        Operand[] array = processStack.toArray(Operand[]::new);
        processStack.clear();
        Operand top = operandStack.peek();
        if (top instanceof IfOperand == false && top instanceof ElseIfOperand == false && top instanceof ForOperand == false && top instanceof ElseOperand == false)
        {
            throw new IllegalStateException("表达式解析异常，异常位置" + fragment);
        }
        if (top instanceof IfOperand ifOperand)
        {
            ifOperand.setBody(new MethodStructureOperand(array, false));
        }
        else if (top instanceof ElseIfOperand elseIfOperand)
        {
            elseIfOperand.setBody(new MethodStructureOperand(array, false));
            operandStack.pop();
            if (operandStack.peek() instanceof IfOperand ifOperand)
            {
                ifOperand.addElseIfOperand(elseIfOperand);
            }
            else
            {
                throw new IllegalStateException("表达式解析异常else if 未能发现与之匹配的 if，异常位置" + fragment);
            }
        }
        else if (top instanceof ElseOperand elseOperand)
        {
            elseOperand.setBody(new MethodStructureOperand(array, false));
            operandStack.pop();
            if (operandStack.peek() instanceof IfOperand ifOperand)
            {
                ifOperand.setElseOperand(elseOperand);
            }
            else
            {
                throw new IllegalStateException("表达式解析异常 else 未能发现与之匹配的 if，异常位置" + fragment);
            }
        }
        else if (top instanceof ForOperand forOperand)
        {
            forOperand.setBody(new MethodStructureOperand(array, false));
        }
    }

    @Override
    public boolean isBoundary()
    {
        return true;
    }
}
