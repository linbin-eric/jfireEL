package cc.jfire.el.expression.impl.operand.property;

import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.impl.operand.ClassOperand;
import cc.jfire.el.expression.impl.operand.VariableOperand;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class StaticClassPropertyOperand implements Operand
{
    private final Object staticClassPropertyValue;

    @SneakyThrows
    public StaticClassPropertyOperand(Operand typeOperand, VariableOperand propertyNameOperand, String fragment)
    {
        Class<?> ckass = ((ClassOperand) typeOperand).getCkass();
        Field    field = Operand.findField(ckass, propertyNameOperand.getVariable(), fragment);
        if (Modifier.isStatic(field.getModifiers()) == false)
        {
            throw new IllegalArgumentException("解析属性异常，非 static 属性不能获取。异常错误位置在:[" + fragment + "]");
        }
        field.setAccessible(true);
        staticClassPropertyValue = field.get(null);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return staticClassPropertyValue;
    }
}
