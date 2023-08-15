package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operand;
import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.MethodInvokeOperand;
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
        parseContext.getOperatorStack().push(this);
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
            if (processStack.peek() instanceof StaticClassOperand)
            {
                StaticClassOperand staticClassOperand = (StaticClassOperand) processStack.pop();
                VariableOperand    variableOperand    = (VariableOperand) processStack.pop();
                parseContext.getOperandStack().push(new MethodInvokeOperand.StaticMethodInvokeOperand(staticClassOperand.getCkass(), variableOperand.getVariable(), processStack.stream().toList(), fragment));
                processStack.clear();
            }
            else if (processStack.peek() instanceof VariableOperand)
            {
                VariableOperand instance   = (VariableOperand) processStack.pop();
                VariableOperand methodName = (VariableOperand) processStack.pop();
                parseContext.getOperandStack().push(new MethodInvokeOperand.InstanceMethodInvokeOperand(instance, methodName, processStack.stream().toList(), fragment));
                processStack.clear();
            }
            else
            {
                throw new IllegalArgumentException("解析过程中方法调用解析异常，方法调用的对象不是静态类也不是变量名称。异常解析位置为" + fragment);
            }
        }
        else
        {
        }
    }
}
