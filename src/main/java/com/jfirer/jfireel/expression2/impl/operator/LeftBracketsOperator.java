package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operand;
import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.DirectMethodOperand;
import com.jfirer.jfireel.expression2.impl.operand.MethodInvokeOperand;
import com.jfirer.jfireel.expression2.impl.operand.StaticClassOperand;
import com.jfirer.jfireel.expression2.impl.operand.VariableOperand;
import lombok.Data;

import java.util.Deque;
import java.util.List;

@Data
public class LeftBracketsOperator implements Operator
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
        }
        else
        {
            type = PURE_LEFT_BRACKETS;
        }
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
            case STATIC_METHOD ->
            {
                Deque<Operand>     operandStack = parseContext.getOperandStack();
                VariableOperand    methodName   = (VariableOperand) operandStack.pop();
                StaticClassOperand pop          = (StaticClassOperand) operandStack.pop();
                Deque<Operand>     processStack = parseContext.getProcessStack();
                List<Operand>      list         = processStack.stream().toList();
                processStack.clear();
                parseContext.getOperandStack().push(new MethodInvokeOperand.StaticMethodInvokeOperand(pop, methodName, list, fragment));
            }
            case INSTANCE_METHOD ->
            {
                Deque<Operand>  operandStack = parseContext.getOperandStack();
                VariableOperand methodName   = (VariableOperand) operandStack.pop();
                VariableOperand pop          = (VariableOperand) operandStack.pop();
                Deque<Operand>  processStack = parseContext.getProcessStack();
                List<Operand>   list         = processStack.stream().toList();
                processStack.clear();
                parseContext.getOperandStack().push(new MethodInvokeOperand.InstanceMethodInvokeOperand(pop, methodName, list, fragment));
            }
            case PURE_LEFT_BRACKETS ->
            {
                ;
            }
        }
    }
}
