package cc.jfire.el.expression.impl.operator;

import cc.jfire.el.PlaceHolder;
import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.Operator;
import cc.jfire.el.expression.ParseContext;
import lombok.Data;

import java.util.Deque;

@Data
public abstract class AbstractRightOperator implements Operator
{
    protected final Class<?>    leftOperatorType;
    protected final PlaceHolder placeHolder;

    @Override
    public void push(ParseContext parseContext)
    {
        Deque<Operator> operatorStack = parseContext.getOperatorStack();
        while (leftOperatorType.isInstance(operatorStack.peek()) == false)
        {
            operatorStack.pop().onPop(parseContext);
        }
        Deque<Operand> processStack = parseContext.getProcessStack();
        Deque<Operand> operandStack = parseContext.getOperandStack();
        while (operandStack.peek() != placeHolder)
        {
            processStack.push(operandStack.pop());
        }
        operandStack.pop();//弹出占位符
        operatorStack.pop().onPop(parseContext);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        throw new UnsupportedOperationException();
    }
}
