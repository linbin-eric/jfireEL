package cc.jfire.el.expression.impl.operator;

import cc.jfire.el.PlaceHolder;
import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.Operator;
import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operand.*;
import lombok.Data;

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
    public static final int    CALL_OPERAND       = 8;
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
        if (parseContext.getRecognizeToken().peekLast() instanceof CallOperandPlaceHolder)
        {
            type = CALL_OPERAND;
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
        else if (parseContext.getRecognizeToken().peekLast() instanceof IfOperand)
        {
            type = IF;
        }
        else if (parseContext.getRecognizeToken().peekLast() instanceof ForOperand)
        {
            type = FOR;
        }
        else if (parseContext.getRecognizeToken().peekLast() instanceof ElseIfOperand)
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
        parseContext.getOperandStack().push(PlaceHolder.LEFT_PAREN);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        switch (type)
        {
            case CALL_OPERAND ->
            {
                Deque<Operand> processStack = parseContext.getProcessStack();
                List<Operand>  list         = processStack.stream().toList();
                processStack.clear();
                ((CallOperandPlaceHolder) parseContext.getOperandStack().peek()).setArgs(list.toArray(Operand[]::new));
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
