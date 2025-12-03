package cc.jfire.el.expression.impl.operand.basic.math.standard;

import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.impl.operand.basic.BasicOperandImpl;

import java.util.Map;

public class OrOperand extends BasicOperandImpl
{
    public OrOperand(String operator, Operand left, Operand right, String fragment)
    {
        super(operator, left, right, fragment);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Object leftValue = left.calculate(contextParam);
        if (leftValue instanceof Boolean b)
        {
            if (b.booleanValue())
            {
                return true;
            }
            else
            {
                Object rightValue = right.calculate(contextParam);
                if (rightValue instanceof Boolean b2)
                {
                    return b2.booleanValue();
                }
                else
                {
                    throw new IllegalStateException("操作数解析出现异常，|| 操作符要求左右参数都是 Boolean。异常解析位置为" + fragment);
                }
            }
        }
        else
        {
            throw new IllegalStateException("操作数解析出现异常，|| 操作符要求左右参数都是 Boolean。异常解析位置为" + fragment);
        }
    }
}
