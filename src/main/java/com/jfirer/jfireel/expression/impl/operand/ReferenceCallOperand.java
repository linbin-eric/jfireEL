package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.baseutil.STR;
import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.baseutil.smc.SmcHelper;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import com.jfirer.jfireel.expression.Operand;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Data
public abstract class ReferenceCallOperand extends CallOperand
{
    private        Method                                                   method;
    private        boolean                                                  supportVariableParams;
    private static ConcurrentMap<String, Constructor<ReferenceCallOperand>> MAKED = new ConcurrentHashMap<>();

    @SneakyThrows
    public static ReferenceCallOperand make(Method method, Operand[] args)
    {
        String key = method.toGenericString() + args.length;
        Constructor<ReferenceCallOperand> referenceCallOperandConstructor = MAKED.computeIfAbsent(key, k -> {
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
                if (method.getParameterTypes()[method.getParameterCount() - 1].isArray() && method.getParameterCount() <= args.length)
                {
                    for (int i = 0; i < method.getParameterCount() - 1; i++)
                    {
                        builder.append("(").append(SmcHelper.getReferenceName(method.getParameterTypes()[i], classModel)).append(")args[").append(i).append("].calculate(contextMap),");
                    }
                    builder.append("new Object[]{");
                    for (int i = method.getParameterCount() - 1; i < args.length; i++)
                    {
                        builder.append("args[").append(i).append("].calculate(contextMap)");
                        if (i != args.length - 1)
                        {
                            builder.append(",");
                        }
                    }
                    builder.append("});");
                }
                else
                {
                    for (int i = 0; i < method.getParameterCount(); i++)
                    {
                        builder.append("(").append(SmcHelper.getReferenceName(method.getParameterTypes()[i], classModel)).append(")args[").append(i).append("].calculate(contextMap)");
                        if (i != method.getParameterCount() - 1)
                        {
                            builder.append(",");
                        }
                    }
                    builder.append(");");
                }
                if (method.getReturnType() == void.class)
                {
                    builder.append("return null;");
                }
                model.setBody(builder.toString());
                classModel.putMethodModel(model);
            }
            try
            {
                Class<ReferenceCallOperand>       compile     = (Class<ReferenceCallOperand>) COMPILE_HELPER.compile(classModel);
                Constructor<ReferenceCallOperand> constructor = compile.getConstructor();
                return constructor;
            }
            catch (ClassNotFoundException | IOException | NoSuchMethodException e)
            {
                ReflectUtil.throwException(e);
                return null;
            }
        });
        try
        {
            ReferenceCallOperand referenceCallOperand = referenceCallOperandConstructor.newInstance();
            referenceCallOperand.setArgs(args);
            method.setAccessible(true);
            referenceCallOperand.setMethod(method);
            referenceCallOperand.setSupportVariableParams(method.getParameterCount() != 0 && method.getParameterTypes()[method.getParameterCount() - 1].isArray());
            return referenceCallOperand;
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public Object calculate(Object[] args)
    {
        if (supportVariableParams)
        {
            Object[] args2 = new Object[method.getParameterCount()];
            System.arraycopy(args, 0, args2, 0, method.getParameterCount() - 1);
            Object[] left = new Object[args.length - method.getParameterCount() + 1];
            System.arraycopy(args, method.getParameterCount() - 1, left, 0, left.length);
            args2[method.getParameterCount() - 1] = left;
            return method.invoke(null, args2);
        }
        else
        {
            return method.invoke(null, args);
        }
    }
}
