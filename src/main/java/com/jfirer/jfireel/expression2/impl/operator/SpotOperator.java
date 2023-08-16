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
            Operand         pop        = processStack.pop();
            VariableOperand methodName = (VariableOperand) processStack.pop();
            if (pop instanceof StaticClassOperand)
            {
                parseContext.getOperandStack().push(new MethodInvokeOperand.StaticMethodInvokeOperand((StaticClassOperand) pop, methodName, processStack.stream().toList(), fragment));
            }
            else if (pop instanceof VariableOperand)
            {
                parseContext.getOperandStack().push(new MethodInvokeOperand.InstanceMethodInvokeOperand((VariableOperand) pop, methodName, processStack.stream().toList(), fragment));
            }
            else
            {
                throw new IllegalArgumentException("解析过程中方法调用解析异常，方法调用的对象不是静态类也不是变量名称。异常解析位置为" + fragment);
            }
            processStack.clear();
        }
        else
        {
            Operand         typeOperand     = (StaticClassOperand) processStack.pop();
            VariableOperand variableOperand = (VariableOperand) processStack.pop();
            if (processStack.peek() instanceof StaticClassOperand)
            {
                parseContext.getOperandStack().push(new PropertyReadOperand.StaticClassPropertyOperand(typeOperand, variableOperand, fragment));
            }
            else
            {
                parseContext.getOperandStack().push(new PropertyReadOperand.InstancePropertyReadOperand(typeOperand, variableOperand, fragment));
            }
        }
    }
}
