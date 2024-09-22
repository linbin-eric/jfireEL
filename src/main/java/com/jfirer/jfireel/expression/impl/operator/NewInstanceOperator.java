package com.jfirer.jfireel.expression.impl.operator;

import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.Operator;
import com.jfirer.jfireel.expression.ParseContext;
import com.jfirer.jfireel.expression.impl.operand.ClassOperand;
import com.jfirer.jfireel.expression.impl.operand.method.compile.CompileConstructorMethod;
import com.jfirer.jfireel.expression.impl.operand.method.standard.ConstructorMethod;
import lombok.Data;

import java.util.Deque;

@Data
public class NewInstanceOperator implements Operator
{
    private       ClassOperand classOperand;
    private final String       fragment;

    @Override
    public int priority()
    {
        return 12;
    }

    @Override
    public void push(ParseContext parseContext)
    {
        parseContext.getOperatorStack().push(this);
    }

    @Override
    public void onPop(ParseContext parseContext)
    {
        Deque<Operand> processStack = parseContext.getProcessStack();
        ClassOperand   classOperand = (ClassOperand) processStack.pop();
        Operand[]      array        = processStack.toArray(Operand[]::new);
        processStack.clear();
        if (parseContext.getConfig().isMethodInvokeUseCompile())
        {
            parseContext.getOperandStack().push(new CompileConstructorMethod(classOperand.getCkass(), array, fragment, parseContext.getConfig()));
        }
        else
        {
            parseContext.getOperandStack().push(new ConstructorMethod(classOperand.getCkass(), array, fragment, parseContext.getMethodInvokeAccelerators()));
        }
    }
}
