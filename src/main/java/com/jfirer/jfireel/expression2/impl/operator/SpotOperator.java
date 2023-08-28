package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operand;
import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.MethodInvokeOperand;
import com.jfirer.jfireel.expression2.impl.operand.PropertyReadOperand;
import com.jfirer.jfireel.expression2.impl.operand.StaticClassOperand;
import com.jfirer.jfireel.expression2.impl.operand.VariableOperand;
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
            if (pop instanceof StaticClassOperand staticClassOperand)
            {
                parseContext.getOperandStack().push(new MethodInvokeOperand.StaticMethod(staticClassOperand.getStaticClass(), methodName, processStack.stream().toList(), fragment));
            }
            else
            {
                if (parseContext.isMethodInvokeUseCompile())
                {
                    parseContext.getOperandStack().push(new MethodInvokeOperand.CompileInstanceMethod(pop, methodName, processStack.stream().toList(), fragment));
                }
                else
                {
                    parseContext.getOperandStack().push(new MethodInvokeOperand.InstanceMethod(pop, methodName, processStack.stream().toList(), fragment));
                }
            }
            processStack.clear();
        }
        else
        {
            Operand         typeOperand     = processStack.pop();
            VariableOperand variableOperand = (VariableOperand) processStack.pop();
            if (typeOperand instanceof StaticClassOperand)
            {
                parseContext.getOperandStack().push(new PropertyReadOperand.StaticClassPropertyOperand(typeOperand, variableOperand, fragment + "." + variableOperand.getVariable()));
            }
            else
            {
                parseContext.getOperandStack().push(new PropertyReadOperand.InstancePropertyReadOperand(typeOperand, variableOperand, fragment + "." + variableOperand.getVariable(), parseContext.isPropertyReadUseLambda()));
            }
        }
    }
}
