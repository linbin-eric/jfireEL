package cc.jfire.el.expression.impl.operator;

import cc.jfire.el.expression.Operator;
import cc.jfire.el.expression.ParseContext;
import lombok.Data;

import java.util.Deque;

@Data
public class CommaOperator implements Operator
{
    private final String fragment;

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
            if (peek instanceof LeftParenOperator || peek instanceof CommaOperator || peek instanceof LeftBracketOperator || peek instanceof  SetStartOperator)
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
    }
}
