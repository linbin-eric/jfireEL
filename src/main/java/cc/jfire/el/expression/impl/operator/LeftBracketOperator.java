package cc.jfire.el.expression.impl.operator;

import cc.jfire.el.PlaceHolder;
import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.Operator;
import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operand.ArrayOperand;
import cc.jfire.el.expression.impl.operand.ContainerOperand;
import cc.jfire.el.expression.impl.operand.VariableOperand;
import cc.jfire.el.expression.impl.operand.property.CompilePropertyReadOperand;
import cc.jfire.el.expression.impl.operand.property.InstancePropertyReadOperand;
import cc.jfire.el.expression.impl.operand.property.StaticClassPropertyOperand;
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
