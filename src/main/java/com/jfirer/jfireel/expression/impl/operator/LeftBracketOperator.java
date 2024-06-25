package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.PlaceHolder;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.TokenType;
import com.jfirer.jfireel.expression.impl.operand.ArrayOperand;
import com.jfirer.jfireel.expression.impl.operand.ContainerOperand;
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
        Operator peek = parseContext.getOperatorStack().peek();
        //不存在左侧，或者左侧是边界符号的情况下，判定当前的[是用于数组的
        TokenType lastToken = parseContext.getLastToken();
        array = lastToken == TokenType.NONE || lastToken == TokenType.OPERATOR;
        parseContext.getOperatorStack().push(this);
        parseContext.getOperandStack().push(PlaceHolder.LEFT_BRACKET);
        parseContext.setLastToken(TokenType.OPERATOR);
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
            parseContext.setLastToken(TokenType.OPERAND);
        }
        else
        {
            Deque<Operand> processStack = parseContext.getProcessStack();
            Operand        index        = processStack.pop();
            Deque<Operand> operandStack = parseContext.getOperandStack();
            Operand        container    = operandStack.pop();
            operandStack.push(new ContainerOperand(container, index));
            parseContext.setLastToken(TokenType.OPERAND);
        }
    }

    @Override
    public boolean isBoundary()
    {
        return true;
    }
}
