package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.PlaceHolder;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.ContainerOperand;
import lombok.Data;

import java.util.Deque;

/**
 * 用于表达容器左侧的'['
 */
@Data
public class ContainerLeftBracketOperator implements Operator
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
        parseContext.getOperandStack().push(PlaceHolder.LeftBracketPlaceHolder);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Deque<Operand> processStack = parseContext.getProcessStack();
        Operand        index        = processStack.pop();
        Deque<Operand> operandStack = parseContext.getOperandStack();
        Operand        container    = operandStack.pop();
        operandStack.push(new ContainerOperand(container, index));
    }

    @Override
    public boolean isBoundary()
    {
        return true;
    }
}
