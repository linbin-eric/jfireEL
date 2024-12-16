package com.jfirer.jfireel.expression.impl;

import com.jfirer.baseutil.STR;
import com.jfirer.baseutil.smc.SmcHelper;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import com.jfirer.jfireel.expression.Operand;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Map;

@Data
public abstract class CompileStaticCallOperand implements Operand
{
    protected Operand[] args;

    @SneakyThrows
    public static Class<? extends CompileStaticCallOperand> make(Method method)
    {
        ClassModel  classModel = new ClassModel(STR.format("{}_{}", "CompileStaticCallOperand", COUNTER.getAndIncrement()), CompileStaticCallOperand.class);
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
        Class<? extends CompileStaticCallOperand> compile = (Class<? extends CompileStaticCallOperand>) COMPILE_HELPER.compile(classModel);
        return compile;
    }
}
