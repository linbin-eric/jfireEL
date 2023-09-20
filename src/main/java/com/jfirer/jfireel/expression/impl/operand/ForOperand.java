package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.ControlFlag;
import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
public class ForOperand implements Operand
{
    private       String  itemName;
    private       Operand itemsContainer;
    private       Operand body;
    private final String  fragment;

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Object calculate = itemsContainer.calculate(contextParam);
        if (calculate instanceof int[] array)
        {
            for (int i : array)
            {
                contextParam.put(itemName, i);
                Object value = body.calculate(contextParam);
                if (value == ControlFlag.RETURN)
                {
                    return value;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
                else if (value instanceof ControlFlagOperand.ReturnWithValue returnWithValue)
                {
                    return returnWithValue.value();
                }
            }
            return null;
        }
        else if (calculate instanceof byte[] array)
        {
            for (byte i : array)
            {
                contextParam.put(itemName, i);
                Object value = body.calculate(contextParam);
                if (value == ControlFlag.RETURN)
                {
                    return value;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
                else if (value instanceof ControlFlagOperand.ReturnWithValue returnWithValue)
                {
                    return returnWithValue.value();
                }
            }
            return null;
        }
        else if (calculate instanceof short[] array)
        {
            for (short i : array)
            {
                contextParam.put(itemName, i);
                Object value = body.calculate(contextParam);
                if (value == ControlFlag.RETURN)
                {
                    return value;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
                else if (value instanceof ControlFlagOperand.ReturnWithValue returnWithValue)
                {
                    return returnWithValue.value();
                }
            }
            return null;
        }
        else if (calculate instanceof long[] array)
        {
            for (long i : array)
            {
                contextParam.put(itemName, i);
                Object value = body.calculate(contextParam);
                if (value == ControlFlag.RETURN)
                {
                    return value;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
                else if (value instanceof ControlFlagOperand.ReturnWithValue returnWithValue)
                {
                    return returnWithValue.value();
                }
            }
            return null;
        }
        else if (calculate instanceof float[] array)
        {
            for (float i : array)
            {
                contextParam.put(itemName, i);
                Object value = body.calculate(contextParam);
                if (value == ControlFlag.RETURN)
                {
                    return value;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
                else if (value instanceof ControlFlagOperand.ReturnWithValue returnWithValue)
                {
                    return returnWithValue.value();
                }
            }
            return null;
        }
        else if (calculate instanceof double[] array)
        {
            for (double i : array)
            {
                contextParam.put(itemName, i);
                Object value = body.calculate(contextParam);
                if (value == ControlFlag.RETURN)
                {
                    return value;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
                else if (value instanceof ControlFlagOperand.ReturnWithValue returnWithValue)
                {
                    return returnWithValue.value();
                }
            }
            return null;
        }
        else if (calculate instanceof char[] array)
        {
            for (char i : array)
            {
                contextParam.put(itemName, i);
                Object value = body.calculate(contextParam);
                if (value == ControlFlag.RETURN)
                {
                    return value;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
                else if (value instanceof ControlFlagOperand.ReturnWithValue returnWithValue)
                {
                    return returnWithValue.value();
                }
            }
            return null;
        }
        else if (calculate instanceof boolean[] array)
        {
            for (boolean i : array)
            {
                contextParam.put(itemName, i);
                Object value = body.calculate(contextParam);
                if (value == ControlFlag.RETURN)
                {
                    return value;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
                else if (value instanceof ControlFlagOperand.ReturnWithValue returnWithValue)
                {
                    return returnWithValue.value();
                }
            }
            return null;
        }
        else if (calculate instanceof Object[] array)
        {
            for (Object i : array)
            {
                contextParam.put(itemName, i);
                Object value = body.calculate(contextParam);
                if (value == ControlFlag.RETURN)
                {
                    return value;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
                else if (value instanceof ControlFlagOperand.ReturnWithValue returnWithValue)
                {
                    return returnWithValue.value();
                }
            }
            return null;
        }
        else if (calculate instanceof Collection<?> collection)
        {
            for (Object o : collection)
            {
                contextParam.put(itemName, o);
                Object value = body.calculate(contextParam);
                if (value == ControlFlag.RETURN)
                {
                    return value;
                }
                else if (value == ControlFlag.BREAK)
                {
                    return null;
                }
                else if (value == ControlFlag.CONTINUE)
                {
                    continue;
                }
                else if (value instanceof ControlFlagOperand.ReturnWithValue returnWithValue)
                {
                    return returnWithValue.value();
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
