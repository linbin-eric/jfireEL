package com.jfirer.jfireel.expression.impl.operand.property;

import com.jfirer.baseutil.STR;
import com.jfirer.baseutil.reflect.valueaccessor.ValueAccessor;
import com.jfirer.baseutil.smc.SmcHelper;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.ConstructorModel;
import com.jfirer.baseutil.smc.model.FieldModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.VariableOperand;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Map;

public class CompilePropertyReadOperand implements Operand
{
    private volatile Operand handler;

    public CompilePropertyReadOperand(Operand typeOperand, VariableOperand propertyNameOperand, String fragment)
    {
        handler = new OneshotForAnalyse(typeOperand, propertyNameOperand.getVariable(), fragment);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return handler.calculate(contextParam);
    }

    @AllArgsConstructor
    class OneshotForAnalyse implements Operand
    {
        private Operand instanceOperand;
        private String  fieldName;
        private String  fragment;

        @SneakyThrows
        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object instance = instanceOperand.calculate(contextParam);
            Field  field    = Operand.findField(instance.getClass(), fieldName, fragment);
            field.setAccessible(true);
            ClassModel classModel = new ClassModel("CompilePropertyReadOperand_" + fieldName + "_" + COUNTER.getAndIncrement());
            classModel.addInterface(Operand.class);
            classModel.addField(new FieldModel("instanceOperand", Operand.class, classModel));
            ConstructorModel constructorModel = new ConstructorModel(classModel);
            constructorModel.setParamTypes(Operand.class);
            constructorModel.setParamNames("instanceOperand");
            constructorModel.setBody("this.instanceOperand = instanceOperand;");
            classModel.addConstructor(constructorModel);
            MethodModel methodModel = new MethodModel(Operand.class.getDeclaredMethod("calculate", Map.class), classModel);
            methodModel.setParamterNames("contextParam");
            String referenceName = SmcHelper.getReferenceName(field.getDeclaringClass(), classModel);
            String methodName    = field.getType() == boolean.class ? "is" + ValueAccessor.toMethodName(field) : "get" + ValueAccessor.toMethodName(field);
            String format = STR.format("""
                                               {} instance = ({})instanceOperand.calculate(contextParam);
                                               return instance.{}();""", referenceName, referenceName, methodName);
            methodModel.setBody(format);
            classModel.putMethodModel(methodModel);
            Class<?> compile = COMPILE_HELPER.compile(classModel);
            Operand  operand = (Operand) compile.getConstructor(Operand.class).newInstance(instanceOperand);
            CompilePropertyReadOperand.this.handler = operand;
            return field.get(instance);
        }
    }
}
