package cc.jfire.el.expression.impl.operand;

import cc.jfire.baseutil.STR;
import cc.jfire.baseutil.reflect.ReflectUtil;
import cc.jfire.baseutil.smc.SmcHelper;
import cc.jfire.baseutil.smc.model.ClassModel;
import cc.jfire.baseutil.smc.model.MethodModel;
import cc.jfire.el.expression.Operand;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Data
public abstract class ReferenceCallOperand extends CallOperand
{
    private        Method                                                   method;
    private        Class<?>                                                 varArgType;
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
                if (method.getParameters()[method.getParameterCount() - 1].isVarArgs())
                {
                    for (int i = 0; i < method.getParameterCount() - 1; i++)
                    {
                        builder.append("(").append(SmcHelper.getReferenceName(method.getParameterTypes()[i], classModel)).append(")args[").append(i).append("].calculate(contextMap),");
                    }
                    Class<?> parameterType = method.getParameterTypes()[method.getParameterCount() - 1];
                    builder.append("new ").append(SmcHelper.getReferenceName(parameterType, classModel)).append("{");
                    Class<?> componentType = parameterType.getComponentType();
                    for (int i = method.getParameterCount() - 1; i < args.length; i++)
                    {
                        builder.append("(").append(SmcHelper.getReferenceName(componentType, classModel)).append(")args[").append(i).append("].calculate(contextMap)");
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
            referenceCallOperand.parse(method);
            referenceCallOperand.setSupportVariableParams(method.getParameterCount() >= 1 && method.getParameters()[method.getParameterCount() - 1].isVarArgs());
            return referenceCallOperand;
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public void parse(Method method)
    {
        this.method = method;
        if (method.getParameterCount() > 0)
        {
            Parameter parameter = method.getParameters()[method.getParameterCount() - 1];
            if (parameter.isVarArgs())
            {
                supportVariableParams = true;
                varArgType            = parameter.getType().getComponentType();
            }
        }
    }

    @SneakyThrows
    public Object calculate(Object[] args)
    {
        if (supportVariableParams)
        {
            Object[] args2 = new Object[method.getParameterCount()];
            System.arraycopy(args, 0, args2, 0, method.getParameterCount() - 1);
            Object[] left = (Object[]) Array.newInstance(varArgType, args.length - method.getParameterCount() + 1);
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
