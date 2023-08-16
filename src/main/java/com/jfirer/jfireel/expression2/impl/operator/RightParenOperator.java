package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operand;
import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;
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
        /**
         * 1、弹出栈顶的非（操作符，对于每一个操作符执行出栈操作。
         * 2、通过字符位置，判断原表达式中，配对的()之间是否为空。如果不为空，则弹出操作数栈，将元素压入操作数缓存栈。
         * 3、对（执行出栈
         */
        Deque<Operator> operatorStack = parseContext.getOperatorStack();
        while (operatorStack.peek() instanceof LeftParenOperator == false)
        {
            operatorStack.pop().onPop(parseContext);
        }
        if (operatorStack.isEmpty())
        {
            throw new IllegalStateException("表达式存在异常，)没有对应匹配的(，异常位置" + fragment);
        }
        LeftParenOperator leftParenOperator = (LeftParenOperator) operatorStack.peek();
        String            sub               = fragment.substring(leftParenOperator.getFragment().length()+1, fragment.length());
        if (sub.trim().equals(")") == false)
        {
            Operand pop = parseContext.getOperandStack().pop();
            parseContext.getProcessStack().push(pop);
        }
        operatorStack.pop().onPop(parseContext);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
    }
}
