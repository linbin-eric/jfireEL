package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.PlaceHolder;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.ArrayOperand;
import com.jfirer.jfireel.expression.impl.operand.ContainerOperand;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;
import com.jfirer.jfireel.expression.impl.operand.property.CompilePropertyReadOperand;
import com.jfirer.jfireel.expression.impl.operand.property.InstancePropertyReadOperand;
import com.jfirer.jfireel.expression.impl.operand.property.StaticClassPropertyOperand;
import lombok.Data;

import java.util.Deque;

/**
 * 用于表达容器左侧的'['
 */
@Data
public class LeftBracketOperator implements Operator
{
    private final String  fragment;
    private       boolean array = false;

    @Override
    public int priority()
    {
        return -1;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        Operand peek = parseContext.getOperandStack().peek();
        if (peek instanceof CompilePropertyReadOperand || peek instanceof InstancePropertyReadOperand || peek instanceof StaticClassPropertyOperand//
            || peek instanceof VariableOperand)
        {
            array = false;
        }
        else
        {
            array = true;
        }
        parseContext.getOperatorStack().push(this);
        parseContext.getOperandStack().push(PlaceHolder.LEFT_BRACKET);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        if (array)
        {
            Deque<Operand> processStack = parseContext.getProcessStack();
            Operand[]      operands     = processStack.stream().toArray(Operand[]::new);
            processStack.clear();
            parseContext.getOperandStack().push(new ArrayOperand(operands));
        }
        else
        {
            Deque<Operand> processStack = parseContext.getProcessStack();
            Operand        index        = processStack.pop();
            Deque<Operand> operandStack = parseContext.getOperandStack();
            Operand        container    = operandStack.pop();
            operandStack.push(new ContainerOperand(container, index));
        }
    }

    @Override
    public boolean isBoundary()
    {
        return true;
    }
}
