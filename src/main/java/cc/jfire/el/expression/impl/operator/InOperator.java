package cc.jfire.el.expression.impl.operator;

import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.Operator;
import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operand.InOperand;
import cc.jfire.el.expression.impl.operand.VariableOperand;

import java.util.Deque;

public class InOperator implements Operator
{
    @Override
    public int priority()
    {
        return -1;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        parseContext.getOperatorStack().push(this);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Deque<Operand>  operandStack   = parseContext.getOperandStack();
        Operand         itemsContainer = operandStack.pop();
        VariableOperand itemName       = (VariableOperand) operandStack.pop();
        operandStack.push(new InOperand(itemName.getVariable(), itemsContainer));
    }
}
