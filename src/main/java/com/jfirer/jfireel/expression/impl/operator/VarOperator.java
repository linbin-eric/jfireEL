package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.CreateVariableOperand;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;

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
