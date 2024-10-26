package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.PlaceHolder;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import lombok.Data;

import java.util.Deque;

@Data
public abstract class AbstractRightOperator implements Operator
{
    protected final Class<?>    leftOperatorType;
    protected final PlaceHolder placeHolder;

    @Override
    public void push(ParseContext parseContext)
    {
        Deque<Operator> operatorStack = parseContext.getOperatorStack();
        while (leftOperatorType.isInstance(operatorStack.peek()) == false)
        {
            operatorStack.pop().onPop(parseContext);
        }
        Deque<Operand> processStack = parseContext.getProcessStack();
        Deque<Operand> operandStack = parseContext.getOperandStack();
        while (operandStack.peek() != placeHolder)
        {
            processStack.push(operandStack.pop());
        }
        operandStack.pop();//弹出占位符
        operatorStack.pop().onPop(parseContext);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        throw new UnsupportedOperationException();
    }

    public static class RightBraceOperator extends AbstractRightOperator
    {
        private final String fragment;

        public RightBraceOperator(String fragment)
        {
            super(LeftBraceOperator.class, PlaceHolder.LEFT_BRACE);
            this.fragment = fragment;
        }

        @Override
        public int priority()
        {
            return 0;
        }
    }

    public static class RightBracketOperator extends AbstractRightOperator
    {
        private final String fragment;

        public RightBracketOperator(String fragment)
        {
            super(LeftBracketOperator.class, PlaceHolder.LEFT_BRACKET);
            this.fragment = fragment;
        }

        @Override
        public int priority()
        {
            return 0;
        }
    }

    public static class RightParenOperator extends AbstractRightOperator
    {
        private final String fragment;

        public RightParenOperator(String fragment)
        {
            super(LeftParenOperator.class, PlaceHolder.LEFT_PAREN);
            this.fragment = fragment;
        }

        @Override
        public int priority()
        {
            return 0;
        }
    }

    public static class SetEnd extends AbstractRightOperator
    {
        private final String fragment;

        public SetEnd(String fragment)
        {
            super(SetStartOperator.class, PlaceHolder.SET_START);
            this.fragment = fragment;
        }

        @Override
        public int priority()
        {
            return 0;
        }
    }
}
