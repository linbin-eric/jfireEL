package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.*;
import lombok.Data;

import java.util.Deque;

@Data
public class RightAngleBracketOperator implements Operator
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
        Deque<Operand>  operandStack  = parseContext.getOperandStack();
        Deque<Operator> operatorStack = parseContext.getOperatorStack();
        Deque<Operand>  processStack  = parseContext.getProcessStack();
        while (operatorStack.peek() instanceof ArrayLeftAngleOperator == false || operatorStack.peek() instanceof MethodStructLeftAngleBracketOperator == false)
        {
            operatorStack.pop().onPop(parseContext);
        }

    }

    @Override
    public void onPop(ParseContext parseContext)
    {
    }
}
