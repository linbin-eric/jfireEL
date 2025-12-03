package cc.jfire.el.expression.impl.operand;

import cc.jfire.el.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class QuestionOperand implements Operand
{
    private final Operand condition;
    private final Operand left;
    private final Operand right;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Object calculate = condition.calculate(contextParam);
        if ((Boolean) calculate)
        {
            return left.calculate(contextParam);
        }
        else
        {
            return right.calculate(contextParam);
        }
    }

    @Override
    public void clearFragment()
    {
        condition.clearFragment();
        left.clearFragment();
        right.clearFragment();
    }
}
