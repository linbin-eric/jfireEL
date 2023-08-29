package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.*;
import lombok.Data;

import java.util.Deque;

@Data
public class RightAngleBracketOperator implements Operator
{
    private final String fragment;

    @Override
    public int priority()
    {
        return 0;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        Deque<Operand> operandStack = parseContext.getOperandStack();
        Deque<Operand> processStack = parseContext.getProcessStack();
        while (operandStack.peek() instanceof LeftAngleBracketOperand == false)
        {
            Operand pop = operandStack.pop();
            processStack.push(pop);
        }
        operandStack.pop();
        Operand[] array = processStack.toArray(Operand[]::new);
        processStack.clear();
        Operand top = operandStack.peek();
        if (top instanceof IfOperand == false && top instanceof ElseIfOperand == false && top instanceof ForOperand == false && top instanceof ElseOperand == false)
        {
            throw new IllegalStateException("表达式解析异常，异常位置" + fragment);
        }
        if (top instanceof IfOperand ifOperand)
        {
            ifOperand.setBody(new MutliOperand(array));
        }
        else if (top instanceof ElseIfOperand elseIfOperand)
        {
            elseIfOperand.setBody(new MutliOperand(array));
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
            elseOperand.setBody(new MutliOperand(array));
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
            forOperand.setBody(new MutliOperand(array));
        }
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
    }
}
