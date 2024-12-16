package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.baseutil.STR;
import com.jfirer.baseutil.smc.SmcHelper;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Supplier;

@Data
public abstract class ReferenceCallOperand extends CallOperand
{
    @SneakyThrows
    public static Supplier<ReferenceCallOperand> make(Method method)
    {
        ClassModel  classModel = new ClassModel(STR.format("{}_{}", "ReferenceCallOperand", COUNTER.getAndIncrement()), ReferenceCallOperand.class);
        MethodModel model      = new MethodModel(classModel);
        model.setMethodName("calculate");
        model.setParamterTypes(Map.class);
        model.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
        model.setParamterNames("contextMap");
        model.setReturnType(Object.class);
        if (method.getParameterCount() == 0)
        {
            StringBuilder builder = new StringBuilder();
            if (method.getReturnType() == void.class)
            {
                builder.append(SmcHelper.getReferenceName(method.getDeclaringClass(), classModel));
                builder.append(".").append(method.getName()).append("();\r\n");
                builder.append("return null;");
            }
            else
            {
                builder.append("return ").append(SmcHelper.getReferenceName(method.getDeclaringClass(), classModel)).append(".").append(method.getName()).append("();");
            }
            model.setBody(builder.toString());
            classModel.putMethodModel(model);
        }
        else
        {
            StringBuilder builder = new StringBuilder();
            if (method.getReturnType() != void.class)
            {
                builder.append("return ");
            }
            builder.append(SmcHelper.getReferenceName(method.getDeclaringClass(), classModel)).append(".").append(method.getName()).append("(");
            for (int i = 0; i < method.getParameterCount(); i++)
            {
                builder.append("(").append(SmcHelper.getReferenceName(method.getParameterTypes()[i], classModel)).append(")args[").append(i).append("].calculate(contextMap)");
                if (i != method.getParameterCount() - 1)
                {
                    builder.append(",");
                }
            }
            builder.append(");");
            if (method.getReturnType() == void.class)
            {
                builder.append("return null;");
            }
            model.setBody(builder.toString());
            classModel.putMethodModel(model);
        }
        Class<ReferenceCallOperand>       compile     = (Class<ReferenceCallOperand>) COMPILE_HELPER.compile(classModel);
        Constructor<ReferenceCallOperand> constructor = compile.getConstructor();
        return () -> {
            try
            {
                return constructor.newInstance();
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
            {
                throw new RuntimeException(e);
            }
        };
    }
}
