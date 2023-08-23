package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.ControlFlag;
import com.jfirer.jfireel.expression2.Operand;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ForOperand implements Operand
{
    private       String  itemName;
    private       Operand itemsContainer;
    private       Operand body;
    private final String  fragment;

    @Override
    public Object calculate(Map<String, Object> param)
    {
        Object calculate = itemsContainer.calculate(param);
        if (calculate instanceof int[] array)
        {
            for (int i : array)
            {
                param.put(itemName, i);
                Object value = body.calculate(param);
                if (value == ControlFlag.RETURN)
                {
                    return ControlFlag.RETURN;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
            }
            return null;
        }
        else if (calculate instanceof byte[] array)
        {
            for (byte i : array)
            {
                param.put(itemName, i);
                Object value = body.calculate(param);
                if (value == ControlFlag.RETURN)
                {
                    return ControlFlag.RETURN;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
            }
            return null;
        }
        else if (calculate instanceof short[] array)
        {
            for (short i : array)
            {
                param.put(itemName, i);
                Object value = body.calculate(param);
                if (value == ControlFlag.RETURN)
                {
                    return ControlFlag.RETURN;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
            }
            return null;
        }
        else if (calculate instanceof long[] array)
        {
            for (long i : array)
            {
                param.put(itemName, i);
                Object value = body.calculate(param);
                if (value == ControlFlag.RETURN)
                {
                    return ControlFlag.RETURN;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
            }
            return null;
        }
        else if (calculate instanceof float[] array)
        {
            for (float i : array)
            {
                param.put(itemName, i);
                Object value = body.calculate(param);
                if (value == ControlFlag.RETURN)
                {
                    return ControlFlag.RETURN;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
            }
            return null;
        }
        else if (calculate instanceof double[] array)
        {
            for (double i : array)
            {
                param.put(itemName, i);
                Object value = body.calculate(param);
                if (value == ControlFlag.RETURN)
                {
                    return ControlFlag.RETURN;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
            }
            return null;
        }
        else if (calculate instanceof char[] array)
        {
            for (char i : array)
            {
                param.put(itemName, i);
                Object value = body.calculate(param);
                if (value == ControlFlag.RETURN)
                {
                    return ControlFlag.RETURN;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
            }
            return null;
        }
        else if (calculate instanceof boolean[] array)
        {
            for (boolean i : array)
            {
                param.put(itemName, i);
                Object value = body.calculate(param);
                if (value == ControlFlag.RETURN)
                {
                    return ControlFlag.RETURN;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
            }
            return null;
        }
        else if (calculate instanceof Object[] array)
        {
            for (Object i : array)
            {
                param.put(itemName, i);
                Object value = body.calculate(param);
                if (value == ControlFlag.RETURN)
                {
                    return ControlFlag.RETURN;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
            }
            return null;
        }
        else if (calculate instanceof List<?> list)
        {
            for (Object o : list)
            {
                param.put(itemName, o);
                Object value = body.calculate(param);
                if (value == ControlFlag.RETURN)
                {
                    return ControlFlag.RETURN;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
            }
            return null;
        }
        else
        {
            throw new IllegalArgumentException("无法识别for 循环中的循环变量，请检查 EL 表达式的相关位置：" + fragment);
        }
    }
}
