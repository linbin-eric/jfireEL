package cc.jfire.el.expression.impl.operand;

import cc.jfire.el.expression.Operand;
import lombok.Data;

import java.util.Map;

@Data
public class InOperand implements Operand
{
    private final String  itemName;
    private final Operand itemsContainer;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearFragment()
    {
        itemsContainer.clearFragment();
    }
}
