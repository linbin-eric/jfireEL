package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;
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
    public Object calculate(Map<String, Object> param)
    {
        Boolean calculate = (Boolean) condition.calculate(param);
        if (calculate)
        {
            return body.calculate(param);
        }
        else
        {
            if (elseIfOperands != null)
            {
                for (ElseIfOperand elseIfOperand : elseIfOperands)
                {
                    if ((Boolean) elseIfOperand.getCondition().calculate(param))
                    {
                        return elseIfOperand.getBody().calculate(param);
                    }
                }
            }
            if (elseOperand != null)
            {
                return elseOperand.getBody().calculate(param);
            }
            return null;
        }
    }
}
