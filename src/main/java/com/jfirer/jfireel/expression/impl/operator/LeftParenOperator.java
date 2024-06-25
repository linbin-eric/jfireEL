package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.PlaceHolder;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.TokenType;
import com.jfirer.jfireel.expression.impl.operand.*;
import lombok.Data;

import java.awt.event.PaintEvent;
import java.util.Deque;
import java.util.List;

@Data
public class LeftParenOperator implements Operator
{
    public static final int    PURE_LEFT_BRACKETS = 1;
    public static final int    METHOD             = 2;
    public static final int    IF                 = 5;
    public static final int    ELSE_IF            = 6;
    public static final int    FOR                = 7;
    public static final int    INNER_CALL         = 8;
    public static final int    CLASS              = 9;
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
        if (operandStack.peek() instanceof InnerCallOperand)
        {
            type = INNER_CALL;
        }
        else if (parseContext.getOperatorStack().peek() instanceof SpotOperator)
        {
            type = METHOD;
            ((SpotOperator) parseContext.getOperatorStack().peek()).setType(SpotOperator.METHOD);
        }
        else if (parseContext.getOperatorStack().peek() instanceof NewInstanceOperator && parseContext.getOperandStack().peek() instanceof ClassOperand)
        {
            type = CLASS;
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
        else if (operandStack.peek() instanceof VariableOperand)
        {
            throw new IllegalStateException("解析异常，(的左边无法匹配，异常位置:" + fragment);
        }
        else
        {
            type = PURE_LEFT_BRACKETS;
        }
        parseContext.getOperatorStack().push(this);
        parseContext.setLastToken(TokenType.OPERATOR);
        parseContext.getOperandStack().push(PlaceHolder.LEFT_PAREN);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        switch (type)
        {
            case INNER_CALL ->
            {
                Deque<Operand> processStack = parseContext.getProcessStack();
                List<Operand>  list         = processStack.stream().toList();
                processStack.clear();
                InnerCallOperand peek = (InnerCallOperand) parseContext.getOperandStack().peek();
                peek.setMethodParams(list.toArray(Operand[]::new));
            }
            case METHOD ->
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
            case CLASS ->
            {
                Deque<Operator>     operatorStack = parseContext.getOperatorStack();
                NewInstanceOperator pop           = (NewInstanceOperator) operatorStack.pop();
                ClassOperand        classOperand  = (ClassOperand) parseContext.getOperandStack().pop();
                parseContext.getProcessStack().push(classOperand);
                pop.onPop(parseContext);
            }
            case IF ->
            {
                Deque<Operand> processStack = parseContext.getProcessStack();
                if (processStack.size() != 1)
                {
                    throw new IllegalStateException("解析表达式异常，if后面的条件位置不匹配。异常位置" + fragment);
                }
                Operand condition = processStack.pop();
                ((IfOperand) parseContext.getOperandStack().peek()).setCondition(condition);
            }
            case ELSE_IF ->
            {
                Deque<Operand> processStack = parseContext.getProcessStack();
                if (processStack.size() != 1)
                {
                    throw new IllegalStateException("解析表达式异常，else if后面的条件位置不匹配。异常位置" + fragment);
                }
                Operand condition = processStack.pop();
                ((ElseIfOperand) parseContext.getOperandStack().peek()).setCondition(condition);
            }
            case FOR ->
            {
                Deque<Operand> processStack = parseContext.getProcessStack();
                if (processStack.peek() instanceof InOperand == false || processStack.size() != 1)
                {
                    throw new IllegalStateException("解析表达式异常，for后面的条件位置不匹配。异常位置" + fragment);
                }
                InOperand  pop  = (InOperand) processStack.pop();
                ForOperand peek = (ForOperand) parseContext.getOperandStack().peek();
                peek.setItemName(pop.getItemName());
                peek.setItemsContainer(pop.getItemsContainer());
            }
            case PURE_LEFT_BRACKETS ->
            {
                parseContext.getOperandStack().push(parseContext.getProcessStack().pop());
            }
        }
    }
}
