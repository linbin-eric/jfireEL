package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.*;
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
    public static final int    INNER_CALL         = 8;
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
        if (operandStack.peek() instanceof MethodInvokeOperand.UnFinishDirectMethod)
        {
            type = DIRECT_METHOD;
        }
        else if (operandStack.peek() instanceof InnerCallOperand)
        {
            type = INNER_CALL;
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
                MethodInvokeOperand.UnFinishDirectMethod peek = (MethodInvokeOperand.UnFinishDirectMethod) parseContext.getOperandStack().pop();
                parseContext.getOperandStack().push(new MethodInvokeOperand.DirectMethod(peek.getCandidates(), peek.getMethodName(), list.toArray(Operand[]::new), parseContext.isMethodInvokeUseCompile(), peek.getFragment()));
            }
            case INNER_CALL ->
            {
                Deque<Operand> processStack = parseContext.getProcessStack();
                List<Operand>  list         = processStack.stream().toList();
                processStack.clear();
                InnerCallOperand peek = (InnerCallOperand) parseContext.getOperandStack().peek();
                peek.setMethodParams(list.toArray(Operand[]::new));
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
