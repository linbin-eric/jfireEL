package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.CharType;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.ArrayOperand;
import com.jfirer.jfireel.expression.impl.operand.ContainerOperand;
import com.jfirer.jfireel.expression.impl.operand.LeftBracketOperand;
import lombok.Data;

import java.util.Deque;

@Data
public class LeftBracketOperator implements Operator
{
    private final String  fragment;
    private       boolean array;

    @Override
    public int priority()
    {
        return -1;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        parseContext.getOperatorStack().push(this);
        array = !CharType.isAlphabet(fragment.charAt(fragment.length() - 2));
        parseContext.getOperandStack().push(new LeftBracketOperand());
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
            Operand        index        = parseContext.getProcessStack().pop();
            Deque<Operand> operandStack = parseContext.getOperandStack();
            Operand        container    = operandStack.pop();
            operandStack.push(new ContainerOperand(container, index));
        }
    }
}
