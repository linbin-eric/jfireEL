package cc.jfire.el.expression.impl.operand;

import cc.jfire.el.expression.Matrix;
import cc.jfire.el.expression.Operand;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class CallOperandPlaceHolder implements Operand
{
    private String    name;
    private Matrix    matrix;
    private Operand[] args;
    private Operand   operand = new OneShot();

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return operand.calculate(contextParam);
    }

    class OneShot implements Operand
    {
        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object[] argValues = new Object[args.length];
            for (int i = 0; i < args.length; i++)
            {
                argValues[i] = args[i].calculate(contextParam);
            }
            CallOperand callOperand = matrix.findCallOperand(name, args, argValues);
            CallOperandPlaceHolder.this.operand = callOperand;
            return callOperand.calculate(argValues);
        }
    }
}
