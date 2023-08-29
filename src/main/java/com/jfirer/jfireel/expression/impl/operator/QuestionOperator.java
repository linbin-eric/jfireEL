package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.QuestionOperand;

import java.util.Deque;

public class QuestionOperator implements Operator
{
    @Override
    public int priority()
    {
        return -1;
    }

    @Override
    public boolean isBoundary()
    {
        return true;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        Deque<Operator> operatorStack = parseContext.getOperatorStack();
        do
        {
            Operator peek = operatorStack.peek();
            if (peek != null && !peek.isBoundary())
            {
                Operator pop = operatorStack.pop();
                pop.onPop(parseContext);
            }
            else
            {
                break;
            }
        } while (true);
        parseContext.getOperatorStack().push(this);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Operand condition = parseContext.getOperandStack().pop();
        Operand left      = parseContext.getProcessStack().pop();
        Operand right     = parseContext.getProcessStack().pop();
        parseContext.getOperandStack().push(new QuestionOperand(condition, left, right));
    }
}
