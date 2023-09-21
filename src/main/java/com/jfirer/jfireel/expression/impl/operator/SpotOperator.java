package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.ClassOperand;
import com.jfirer.jfireel.expression.impl.operand.MethodInvokeOperand;
import com.jfirer.jfireel.expression.impl.operand.PropertyReadOperand;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;
import lombok.Data;

import java.util.Deque;

@Data
public class SpotOperator implements Operator
{
    public static final int    METHOD   = 1;
    public static final int    PROPERTY = 2;
    private             int    type     = PROPERTY;
    private final       String fragment;

    @Override
    public int priority()
    {
        return 11;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        Deque<Operator> operatorStack = parseContext.getOperatorStack();
        while (operatorStack.isEmpty() == false && operatorStack.peek().priority() >= priority())
        {
            operatorStack.pop().onPop(parseContext);
        }
        operatorStack.push(this);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Deque<Operand> operandStack = parseContext.getOperandStack();
        Deque<Operand> processStack = parseContext.getProcessStack();
        processStack.push(operandStack.pop());
        processStack.push(operandStack.pop());
        if (type == METHOD)
        {
            Operand pop        = processStack.pop();
            String  methodName = ((VariableOperand) processStack.pop()).getVariable();
            if (pop instanceof ClassOperand classOperand)
            {
                parseContext.getOperandStack().push(new MethodInvokeOperand.StaticMethod(classOperand.getCkass(), methodName, processStack.stream().toArray(Operand[]::new), fragment, parseContext.getMethodInvokeAccelerators()));
            }
            else
            {
                parseContext.getOperandStack().push(new MethodInvokeOperand.InstanceMethod(pop, methodName, processStack.stream().toArray(Operand[]::new), fragment, parseContext.getMethodInvokeAccelerators()));
            }
            processStack.clear();
        }
        else
        {
            Operand         typeOperand     = processStack.pop();
            VariableOperand variableOperand = (VariableOperand) processStack.pop();
            if (typeOperand instanceof ClassOperand)
            {
                parseContext.getOperandStack().push(new PropertyReadOperand.StaticClassPropertyOperand(typeOperand, variableOperand, fragment + "." + variableOperand.getVariable(), parseContext.getPropertyReadAccelerators()));
            }
            else
            {
                parseContext.getOperandStack().push(new PropertyReadOperand.InstancePropertyReadOperand(typeOperand, variableOperand, fragment + "." + variableOperand.getVariable(),  parseContext.getPropertyReadAccelerators()));
            }
        }
    }
}
