package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operand;
import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.*;
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
    public static final int    IF                 = 5;
    public static final int    ELSE_IF            = 6;
    public static final int    FOR                = 7;
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
        if (operandStack.peek() instanceof MethodInvokeOperand.DirectMethod)
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
        else if (operandStack.peek() instanceof IfOperand)
        {
            type = IF;
        }
        else if (operandStack.peek() instanceof ForOperand)
        {
            type = FOR;
        }
        else if (operandStack.peek() instanceof ElseIfOperand)
        {
            type = ELSE_IF;
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
                MethodInvokeOperand.DirectMethod peek = (MethodInvokeOperand.DirectMethod) parseContext.getOperandStack().peek();
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
            case IF ->
            {
                Deque<Operand> operandStack = parseContext.getOperandStack();
                Operand        condition    = operandStack.pop();
                if (operandStack.peek() instanceof IfOperand == false)
                {
                    throw new IllegalStateException("解析表达式异常，if后面的条件位置不匹配。异常位置" + fragment);
                }
                ((IfOperand) operandStack.peek()).setCondition(condition);
            }
            case ELSE_IF ->
            {
                Deque<Operand> operandStack = parseContext.getOperandStack();
                Operand        condition    = operandStack.pop();
                if (operandStack.peek() instanceof ElseIfOperand == false)
                {
                    throw new IllegalStateException("解析表达式异常，else if后面的条件位置不匹配。异常位置" + fragment);
                }
                ((ElseIfOperand) operandStack.peek()).setCondition(condition);
            }
            case FOR ->
            {
                Deque<Operand> operandStack = parseContext.getOperandStack();
                if (operandStack.peek() instanceof InOperand == false)
                {
                    throw new IllegalStateException("解析表达式异常，for后面的条件位置不匹配。异常位置" + fragment);
                }
                InOperand  pop  = (InOperand) operandStack.pop();
                ForOperand peek = (ForOperand) operandStack.peek();
                peek.setItemName(pop.getItemName());
                peek.setItemsContainer(pop.getItemsContainer());
            }
            case PURE_LEFT_BRACKETS ->
            {
                ;
            }
        }
    }
}
