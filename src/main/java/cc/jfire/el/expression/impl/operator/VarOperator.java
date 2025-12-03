package cc.jfire.el.expression.impl.operator;

import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.Operator;
import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operand.CreateVariableOperand;
import cc.jfire.el.expression.impl.operand.VariableOperand;

import java.util.Deque;

public class VarOperator implements Operator
{
    @Override
    public int priority()
    {
        return 11;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        parseContext.getOperatorStack().push(this);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Deque<Operand>  operandStack = parseContext.getOperandStack();
        if (operandStack.isEmpty() || operandStack.peek() instanceof VariableOperand == false)
        {
            throw new IllegalArgumentException("表达式异常，var 操作符后面没有变量名");
        }
        VariableOperand pop = (VariableOperand) parseContext.getOperandStack().pop();
        parseContext.getOperandStack().push(new CreateVariableOperand(pop.getVariable()));
    }
}
