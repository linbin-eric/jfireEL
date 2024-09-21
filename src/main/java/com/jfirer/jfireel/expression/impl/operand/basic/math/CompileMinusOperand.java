package com.jfirer.jfireel.expression.impl.operand.basic.math;

import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import com.jfirer.jfireel.expression.Operand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiFunction;

public class CompileMinusOperand implements Operand
{
    private volatile Operand operand;

    public CompileMinusOperand(Operand left, Operand right, String fragment)
    {
        operand = new AnalyseOperand(left, right, fragment);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return operand.calculate(contextParam);
    }

    @Data
    class RuntimeOperand implements Operand
    {
        final private Operand                            left;
        final private Operand                            right;
        final         BiFunction<Object, Object, Object> minus;

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue  = left.calculate(contextParam);
            Object rightValue = right.calculate(contextParam);
            return minus.apply(leftValue, rightValue);
        }
    }

    @AllArgsConstructor
    class AnalyseOperand implements Operand
    {
        private Operand left;
        private Operand right;
        private String  fragment;

        @SneakyThrows
        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object     leftValue  = left.calculate(contextParam);
            Object     rightValue = right.calculate(contextParam);
            ClassModel classModel = new ClassModel("MinusOperand_" + COUNTER.getAndIncrement());
            classModel.addImport(Number.class);
            classModel.addInterface(BiFunction.class);
            MethodModel methodModel = new MethodModel(BiFunction.class.getDeclaredMethod("apply", Object.class, Object.class), classModel);
            methodModel.setParamterNames("a", "b");
            StringBuilder builder = new StringBuilder();
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                if (leftValue instanceof BigDecimal || rightValue instanceof BigDecimal)
                {
                    builder.append("return new BigDecimal(a.toString()).subtract(new BigDecimal(a.toString()));");
                }
                if (leftValue instanceof Integer || leftValue instanceof Short || leftValue instanceof Byte)
                {
                    if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
                    {
                        builder.append("return ((Number)a).intValue()- ((Number)b).intValue();");
                    }
                    else if (rightValue instanceof Long b)
                    {
                        builder.append("return ((Number)a).intValue()- ((Number)b).longValue();");
                    }
                    else if (rightValue instanceof Float b)
                    {
                        builder.append("return ((Number)a).intValue()- ((Number)b).floatValue();");
                    }
                    else if (rightValue instanceof Double b)
                    {
                        builder.append("return ((Number)a).intValue()- ((Number)b).doubleValue();");
                    }
                    else
                    {
                        throw new IllegalArgumentException();
                    }
                }
                else if (leftValue instanceof Long)
                {
                    long a = ((Long) leftValue).longValue();
                    if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
                    {
                        builder.append("return ((Number)a).longValue()- ((Number)b).intValue();");
                    }
                    else if (rightValue instanceof Long b)
                    {
                        builder.append("return ((Number)a).longValue()- ((Number)b).longValue();");
                    }
                    else if (rightValue instanceof Float b)
                    {
                        builder.append("return ((Number)a).longValue()- ((Number)b).floatValue();");
                    }
                    else if (rightValue instanceof Double b)
                    {
                        builder.append("return ((Number)a).longValue()- ((Number)b).doubleValue();");
                    }
                    else
                    {
                        throw new IllegalArgumentException();
                    }
                }
                else if (leftValue instanceof Float number)
                {
                    float a = number.floatValue();
                    if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
                    {
                        builder.append("return ((Number)a).floatValue()- ((Number)b).intValue();");
                    }
                    else if (rightValue instanceof Long b)
                    {
                        builder.append("return ((Number)a).floatValue()- ((Number)b).longValue();");
                    }
                    else if (rightValue instanceof Float b)
                    {
                        builder.append("return ((Number)a).floatValue()- ((Number)b).floatValue();");
                    }
                    else if (rightValue instanceof Double b)
                    {
                        builder.append("return ((Number)a).floatValue()- ((Number)b).doubleValue();");
                    }
                    else
                    {
                        throw new IllegalArgumentException();
                    }
                }
                else if (leftValue instanceof Double number)
                {
                    double a = number.doubleValue();
                    if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
                    {
                        builder.append("return ((Number)a).doubleValue()- ((Number)b).intValue();");
                    }
                    else if (rightValue instanceof Long b)
                    {
                        builder.append("return ((Number)a).doubleValue()- ((Number)b).longValue();");
                    }
                    else if (rightValue instanceof Float b)
                    {
                        builder.append("return ((Number)a).doubleValue()- ((Number)b).floatValue();");
                    }
                    else if (rightValue instanceof Double b)
                    {
                        builder.append("return ((Number)a).doubleValue()- ((Number)b).doubleValue();");
                    }
                    else
                    {
                        throw new IllegalArgumentException();
                    }
                }
                else
                {
                    throw new IllegalArgumentException();
                }
                methodModel.setBody(builder.toString());
                classModel.putMethodModel(methodModel);
                Class<BiFunction> compile    = (Class<BiFunction>) COMPILE_HELPER.compile(classModel);
                BiFunction        biFunction = compile.getConstructor().newInstance();
                CompileMinusOperand.this.operand = new RuntimeOperand(left, right, biFunction);
                return biFunction.apply(leftValue, rightValue);
            }
            else
            {
                throw new IllegalStateException("操作数解析出现异常，- 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
            }
        }
    }
}
