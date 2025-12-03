package cc.jfire.el.expression.impl.operator;

import cc.jfire.el.PlaceHolder;
import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.Operator;
import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operand.SetOperand;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Deque;

/**
 * 表示左侧的尖括号&lt;&lt;
 */
@Data
@Accessors(chain = true)
public class SetStartOperator implements Operator
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
        parseContext.getOperandStack().push(PlaceHolder.SET_START);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Deque<Operand> processStack = parseContext.getProcessStack();
        Operand[]      operands     = processStack.stream().toArray(Operand[]::new);
        processStack.clear();
        parseContext.getOperandStack().push(new SetOperand(operands));
    }

    @Override
    public boolean isBoundary()
    {
        return true;
    }
}
