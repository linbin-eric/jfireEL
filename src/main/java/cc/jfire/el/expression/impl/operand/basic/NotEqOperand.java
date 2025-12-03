package cc.jfire.el.expression.impl.operand.basic;

import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.impl.operand.basic.math.standard.EqOperand;

import java.util.Map;

public class NotEqOperand extends BasicOperandImpl
{
    private EqOperand eqOperand;

    public NotEqOperand(String operator, Operand left, Operand right, String fragment)
    {
        super(operator, left, right, fragment);
        eqOperand = new EqOperand(operator, left, right, fragment);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return !(Boolean) eqOperand.calculate(contextParam);
    }

    @Override
    public void clearFragment()
    {
        super.clearFragment();
        eqOperand.clearFragment();
    }
}
