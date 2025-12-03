package cc.jfire.el.expression.impl.operator;

import cc.jfire.el.expression.ELConfig;
import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.Operator;
import cc.jfire.el.expression.ParseContext;
import cc.jfire.el.expression.impl.operand.ClassOperand;
import cc.jfire.el.expression.impl.operand.VariableOperand;
import cc.jfire.el.expression.impl.operand.method.compile.CompileInstanceMethod;
import cc.jfire.el.expression.impl.operand.method.compile.CompileStaticMethod;
import cc.jfire.el.expression.impl.operand.method.standard.InstanceMethod;
import cc.jfire.el.expression.impl.operand.method.standard.StaticMethod;
import cc.jfire.el.expression.impl.operand.property.CompilePropertyReadOperand;
import cc.jfire.el.expression.impl.operand.property.InstancePropertyReadOperand;
import cc.jfire.el.expression.impl.operand.property.StaticClassPropertyOperand;
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
            Operand  pop        = processStack.pop();
            String   methodName = ((VariableOperand) processStack.pop()).getVariable();
            ELConfig config     = parseContext.getConfig();
            if (pop instanceof ClassOperand classOperand)
            {
                if (config.isMethodInvokeUseCompile())
                {
                    parseContext.getOperandStack().push(new CompileStaticMethod(classOperand.getCkass(), processStack.toArray(Operand[]::new), methodName, fragment, config));
                }
                else
                {
                    parseContext.getOperandStack().push(new StaticMethod(classOperand.getCkass(), methodName, processStack.toArray(Operand[]::new), fragment, parseContext.getMatrix()));
                }
            }
            else
            {
                if (config.isMethodInvokeUseCompile())
                {
                    parseContext.getOperandStack().push(new CompileInstanceMethod(pop, methodName, processStack.toArray(Operand[]::new), fragment, parseContext.getConfig()));
                }
                else
                {
                    parseContext.getOperandStack().push(new InstanceMethod(pop, methodName, processStack.toArray(Operand[]::new), fragment, parseContext.getMatrix()));
                }
            }
            processStack.clear();
        }
        else
        {
            Operand         typeOperand     = processStack.pop();
            VariableOperand variableOperand = (VariableOperand) processStack.pop();
            if (typeOperand instanceof ClassOperand)
            {
                parseContext.getOperandStack().push(new StaticClassPropertyOperand(typeOperand, variableOperand, fragment + "." + variableOperand.getVariable()));
            }
            else
            {
                ELConfig config = parseContext.getConfig();
                if (config.isPropertyReadUseCompile())
                {
                    parseContext.getOperandStack().push(new CompilePropertyReadOperand(typeOperand, variableOperand, fragment + "." + variableOperand.getVariable()));
                }
                else
                {
                    parseContext.getOperandStack().push(new InstancePropertyReadOperand(typeOperand, variableOperand.getVariable(), fragment + "." + variableOperand.getVariable(), parseContext.getMatrix()));
                }
            }
        }
    }
}
