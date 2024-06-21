package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.ClassOperand;
import lombok.Data;

import java.util.Deque;

@Data
public class NewInstanceOperator implements Operator
{
    private ClassOperand classOperand;

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
        Deque<Operand> processStack = parseContext.getProcessStack();
        ClassOperand   classOperand = (ClassOperand) processStack.pop();

    }
}
