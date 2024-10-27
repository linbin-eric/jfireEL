package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.AssignOperand;
import com.jfirer.jfireel.expression.impl.operand.CreateVariableOperand;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;
import lombok.Data;

import java.util.Deque;

@Data
public class AssignOperator implements Operator
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
        while (operatorStack.isEmpty() == false && operatorStack.peek().priority() > priority())
        {
            operatorStack.pop().onPop(parseContext);
        }
        parseContext.getOperatorStack().push(this);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Deque<Operand> operandStack = parseContext.getOperandStack();
        Operand        value        = operandStack.pop();
        Operand        pop          = operandStack.pop();
        if (pop instanceof VariableOperand variableOperand)
        {
            operandStack.push(new AssignOperand(variableOperand.getVariable(), value));
        }
        else if (pop instanceof CreateVariableOperand createVariableOperand)
        {
            operandStack.push(new AssignOperand.CreateVariableAndAssignOperand(createVariableOperand.getVariableName(), value));
        }
        else
        {
            throw new IllegalStateException("解析出现异常，在=操作操作符的左侧，应该是 var 操作符或者变量。当前不是这种情况，异常解析地址为:" + fragment);
        }
    }
}
