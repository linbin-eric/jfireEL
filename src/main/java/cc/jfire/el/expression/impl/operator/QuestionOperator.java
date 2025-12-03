package cc.jfire.el.expression.impl.operator;

import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.Operator;
import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operand.QuestionOperand;

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
