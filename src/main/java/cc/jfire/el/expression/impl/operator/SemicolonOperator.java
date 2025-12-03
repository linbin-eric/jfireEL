package cc.jfire.el.expression.impl.operator;

import cc.jfire.el.expression.Operator;
import cc.jfire.el.expression.ParseContext;
import lombok.Data;

import java.util.Deque;

@Data
public class SemicolonOperator implements Operator
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
        if (operatorStack.isEmpty() == false && operatorStack.peek() instanceof ReturnOperator)
        {
            if (fragment.trim().endsWith("return"))
            {
                ((ReturnOperator) operatorStack.peek()).setWithValue(false);
            }
        }
        while (!operatorStack.isEmpty() && operatorStack.peek() instanceof LeftBraceOperator == false)
        {
            operatorStack.pop().onPop(parseContext);
        }
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
    }
}
