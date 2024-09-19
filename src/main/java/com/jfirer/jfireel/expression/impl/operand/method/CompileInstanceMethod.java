package com.jfirer.jfireel.expression.impl.operand.method;

import com.jfirer.baseutil.STR;
import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.baseutil.smc.SmcHelper;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.ConstructorModel;
import com.jfirer.baseutil.smc.model.FieldModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.Operand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class CompileInstanceMethod implements Operand
{
    private volatile Operand operand;

    @Data
    @AllArgsConstructor
    class OneshotAnalyseOperand implements Operand
    {
        private String    methodName;
        private Operand[] argOperands;
        private String    fragment;
        private Operand   instanceOperand;

        @SneakyThrows
        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object instance = instanceOperand.calculate(contextParam);
            if (instance == null)
            {
                throw new IllegalStateException("方法调用，但是调用对象为空，请检查是否变量名错误，异常位置为" + fragment);
            }
            Object[]    args       = Arrays.stream(argOperands).map(operand -> operand.calculate(contextParam)).toArray(Object[]::new);
            Method      executable = (Method) MethodInvokeHelper.findExecutable(Stream.iterate((Class) instance.getClass(), c -> c != Object.class, Class::getSuperclass).flatMap(c -> Arrays.stream(c.getDeclaredMethods())).toList(), args, methodName);
            final int[] classIds   = Arrays.stream(executable.getParameterTypes()).mapToInt(ReflectUtil::getClassId).toArray();
            if (executable == null)
            {
                throw new IllegalArgumentException("解析过程中发现未能发现匹配的方法,方法名为:" + methodName + "。异常解析位置为" + fragment);
            }
            CompileInstanceMethod.this.operand = make(executable, argOperands, instanceOperand, classIds);
            executable.setAccessible(true);
            return executable.invoke(instance, MethodInvokeHelper.compatibleValues(args, classIds));
        }
    }

    public CompileInstanceMethod(Operand instanceOperand, String methodName, Operand[] methodParams, String fragment)
    {
        operand = new OneshotAnalyseOperand(methodName, methodParams, fragment, instanceOperand);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return operand.calculate(contextParam);
    }

    @SneakyThrows
    private Operand make(Method instanceMethod, Operand[] argOperands, Operand instanceOperand, int[] classIds)
    {
        ClassModel classModel = new ClassModel("CompileInstanceMethod_" + COUNTER.getAndIncrement());
        classModel.addInterface(Operand.class);
        classModel.addImport(Map.class);
        classModel.addImport(MethodInvokeHelper.class);
        classModel.addField(new FieldModel("instanceOperand", Operand.class, classModel));
        for (int i = 0; i < argOperands.length; i++)
        {
            classModel.addField(new FieldModel("argOperand_" + i, Operand.class, classModel));
            classModel.addField(new FieldModel("classId_" + i, int.class, classModel));
        }
        ConstructorModel constructorModel = new ConstructorModel(classModel);
        constructorModel.setParamTypes(Operand.class, Operand[].class, int[].class);
        constructorModel.setParamNames("instanceOperand", "argOperands", "classIds");
        StringBuilder builder = new StringBuilder("this.instanceOperand=instanceOperand;\r\n");
        for (int i = 0; i < argOperands.length; i++)
        {
            builder.append("this.argOperand_" + i + " = argOperands[" + i + "];");
            builder.append("this.classId_" + i + " = classIds[" + i + "];");
        }
        constructorModel.setBody(builder.toString());
        classModel.addConstructor(constructorModel);
        Method      calculate   = Operand.class.getDeclaredMethod("calculate", Map.class);
        MethodModel methodModel = new MethodModel(calculate, classModel);
        methodModel.setParamterNames("contextParam");
        String referenceName = SmcHelper.getReferenceName(instanceMethod.getDeclaringClass(), classModel);
        if (instanceMethod.getParameterCount() == 0)
        {
            methodModel.setBody(STR.format("""
                                                   {} instance = ({}) instanceOperand.calculate(contextParam);
                                                   return instance.{}();
                                                   """, referenceName, referenceName, instanceMethod.getName()));
        }
        else
        {
            String format = STR.format("""
                                               {} instance = ({}) instanceOperand.calculate(contextParam);
                                               """, referenceName, referenceName);
            builder = new StringBuilder();
            builder.append("return instance.").append(instanceMethod.getName()).append("(");
            Class<?>[] parameterTypes = instanceMethod.getParameterTypes();
            for (int i = 0; i < instanceMethod.getParameterCount(); i++)
            {
//                builder.append("(").append(SmcHelper.getReferenceName(ReflectUtil.getBoxedTypeOrOrigin(parameterTypes[i]), classModel)).append(")MethodInvokeHelper.compatibleValues(argOperand_").append(i).append(".calculate(contextParam),classId_").append(i).append(")");
                builder.append("(").append(SmcHelper.getReferenceName(ReflectUtil.getBoxedTypeOrOrigin(parameterTypes[i]), classModel)).append(")argOperand_").append(i).append(".calculate(contextParam)");
                if (i != instanceMethod.getParameterCount() - 1)
                {
                    builder.append(",");
                }
            }
            builder.append(");");
            methodModel.setBody(format + builder.toString());
        }
        classModel.putMethodModel(methodModel);
        System.out.println(classModel.toString());
        Class<?> compile = COMPILE_HELPER.compile(classModel);
        return (Operand) compile.getConstructor(Operand.class, Operand[].class, int[].class).newInstance(instanceOperand, argOperands, classIds);
    }
}
