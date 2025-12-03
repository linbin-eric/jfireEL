package cc.jfire.el.expression.impl.operand;

import cc.jfire.el.expression.Operand;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class IfOperand implements Operand
{
    private Operand             condition;
    private Operand             body;
    @Setter(AccessLevel.NONE)
    private List<ElseIfOperand> elseIfOperands;
    private ElseOperand         elseOperand;

    public void addElseIfOperand(ElseIfOperand elseIfOperand)
    {
        if (elseIfOperands == null)
        {
            elseIfOperands = new ArrayList<>();
        }
        elseIfOperands.add(elseIfOperand);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Boolean calculate = (Boolean) condition.calculate(contextParam);
        if (calculate)
        {
            return body.calculate(contextParam);
        }
        else
        {
            if (elseIfOperands != null)
            {
                for (ElseIfOperand elseIfOperand : elseIfOperands)
                {
                    if ((Boolean) elseIfOperand.getCondition().calculate(contextParam))
                    {
                        return elseIfOperand.getBody().calculate(contextParam);
                    }
                }
            }
            if (elseOperand != null)
            {
                return elseOperand.getBody().calculate(contextParam);
            }
            return null;
        }
    }

    @Override
    public void clearFragment()
    {
        condition.clearFragment();
        body.clearFragment();
        if (elseIfOperands != null)
        {
            elseIfOperands.forEach(Operand::clearFragment);
        }
        if (elseOperand != null)
        {
            elseOperand.clearFragment();
        }
    }
}
