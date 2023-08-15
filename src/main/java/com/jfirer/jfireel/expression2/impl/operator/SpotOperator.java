package com.jfirer.jfireel.expression2.impl.operator;

import com.jfirer.jfireel.expression2.Operand;
import com.jfirer.jfireel.expression2.Operator;
import com.jfirer.jfireel.expression2.ParseContext;
import com.jfirer.jfireel.expression2.impl.operand.StaticClassOperand;
import com.jfirer.jfireel.expression2.impl.operand.VariableOperand;
import lombok.Data;

import java.lang.reflect.Method;
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
                int                methodParamCount   = processStack.size();
                Class<?>           ckass              = staticClassOperand.getCkass();
                Method             matchMethod;
                int                matchTime          = 0;
                while (ckass != Object.class)
                {
                    for (Method method : ckass.getMethods())
                    {
                        if (method.getParameterCount() == methodParamCount && method.getName().equalsIgnoreCase(variableOperand.getVariable()))
                        {
                            matchTime += 1;
                            matchMethod = method;
                        }
                    }
                }
                if (matchTime > 1)
                {
                    throw new IllegalArgumentException("解析过程中发现静态类的方法有多个匹配，当前方法重载仅能支持不同入参个数的。异常解析位置为" + fragment);
                }
            }
        }
        else
        {
        }
    }
}
