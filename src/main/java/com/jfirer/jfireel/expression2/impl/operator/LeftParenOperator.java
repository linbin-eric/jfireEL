package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operand;
import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.DirectMethodOperand;
import com.jfirer.jfireel.expression2.impl.operand.StaticClassOperand;
import lombok.Data;

import java.util.Deque;
import java.util.List;

@Data
public class LeftParenOperator implements Operator
{
    public static final int    PURE_LEFT_BRACKETS = 1;
    public static final int    STATIC_METHOD      = 2;
    public static final int    INSTANCE_METHOD    = 3;
    public static final int    DIRECT_METHOD      = 4;
    private final       String fragment;
    private             int    type;

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
        Deque<Operand> operandStack = parseContext.getOperandStack();
        if (operandStack.peek() instanceof DirectMethodOperand)
        {
            type = DIRECT_METHOD;
        }
        else if (parseContext.getOperatorStack().peek() instanceof SpotOperator)
        {
            Operand tmp = operandStack.pop();
            if (operandStack.peek() instanceof StaticClassOperand)
            {
                type = STATIC_METHOD;
            }
            else
            {
                type = INSTANCE_METHOD;
            }
            operandStack.push(tmp);
            ((SpotOperator) parseContext.getOperatorStack().peek()).setType(SpotOperator.METHOD);
        }
        else
        {
            type = PURE_LEFT_BRACKETS;
        }
        parseContext.getOperatorStack().push(this);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        switch (type)
        {
            case DIRECT_METHOD ->
            {
                Deque<Operand> processStack = parseContext.getProcessStack();
                List<Operand>  list         = processStack.stream().toList();
                processStack.clear();
                DirectMethodOperand peek = (DirectMethodOperand) parseContext.getOperandStack().peek();
                peek.setMethodParams(list);
            }
            case STATIC_METHOD, INSTANCE_METHOD ->
            {
                if (parseContext.getOperatorStack().peek() instanceof SpotOperator)
                {
                    parseContext.getOperatorStack().pop().onPop(parseContext);
                }
                else
                {
                    throw new IllegalStateException("解析表达式异常，异常位置" + fragment);
                }
            }
            case PURE_LEFT_BRACKETS ->
            {
                ;
            }
        }
    }
}
