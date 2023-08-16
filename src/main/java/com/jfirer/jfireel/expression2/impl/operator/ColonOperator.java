package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operand;
import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;
import lombok.Data;

import java.util.Deque;

@Data
public class ColonOperator implements Operator
{
    private final String fragment;

    @Override
    public int priority()
    {
        return 10;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        Deque<Operator> operatorStack = parseContext.getOperatorStack();
        do
        {
            Operator peek = operatorStack.peek();
            if (peek instanceof QuestionOperator || peek instanceof CommaOperator || peek instanceof LeftParenOperator || peek instanceof ColonOperator)
            {
                break;
            }
            else
            {
                Operator pop = operatorStack.pop();
                pop.onPop(parseContext);
            }
        } while (true);
        parseContext.getOperatorStack().push(this);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Deque<Operand> processStack = parseContext.getProcessStack();
        Deque<Operand> operandStack = parseContext.getOperandStack();
        processStack.push(operandStack.pop());
        processStack.push(operandStack.pop());
        Deque<Operator> operatorStack = parseContext.getOperatorStack();
        Operator        pop           = operatorStack.pop();
        if (pop instanceof QuestionOperator question)
        {
            question.onPop(parseContext);
        }
        else
        {
            throw new IllegalStateException("表达式存在异常，异常位置为" + fragment);
        }
    }
}
