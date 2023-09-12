package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.LeftParenOperand;
import lombok.Data;

import java.util.Deque;

@Data
public class RightParenOperator implements Operator
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
        Deque<Operator> operatorStack = parseContext.getOperatorStack();
        while (operatorStack.peek() instanceof LeftParenOperator == false)
        {
            operatorStack.pop().onPop(parseContext);
        }
        if (operatorStack.isEmpty())
        {
            throw new IllegalStateException("表达式存在异常，)没有对应匹配的(，异常位置" + fragment);
        }
        Deque<Operand> operandStack = parseContext.getOperandStack();
        while (operandStack.peek() instanceof LeftParenOperand == false)
        {
            parseContext.getProcessStack().push(operandStack.pop());
        }
        operandStack.pop();
        operatorStack.pop().onPop(parseContext);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
    }
}
