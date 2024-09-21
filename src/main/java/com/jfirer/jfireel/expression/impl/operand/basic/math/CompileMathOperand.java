package com.jfirer.jfireel.expression.impl.operand.basic.math;

import com.jfirer.baseutil.STR;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import com.jfirer.jfireel.expression.Operand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiFunction;

public class CompileMathOperand implements ChangeRuntimeOperand, Operand
{
    private volatile Operand runtime;

    public CompileMathOperand(String operator, Operand left, Operand right, String fragment)
    {
        runtime = new AnalyseOperand(left, right, fragment, operator, this);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return runtime.calculate(contextParam);
    }

    @Override
    public void newOperand(Operand operand)
    {
        this.runtime = operand;
    }

    @AllArgsConstructor
    class AnalyseOperand implements Operand
    {
        private Operand              left;
        private Operand              right;
        private String               fragment;
        private String               mathOperator;
        private ChangeRuntimeOperand changeRuntimeOperand;

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
                    switch (mathOperator)
                    {
                        case "-" -> builder.append("return new BigDecimal(a.toString()).subtract(new BigDecimal(a.toString()));");
                        case "+" -> builder.append("return new BigDecimal(leftValue.toString()).add(new BigDecimal(rightValue.toString()));");
                        case "*" -> builder.append("return new BigDecimal(leftValue.toString()).multiply(new BigDecimal(rightValue.toString()));");
                        case "/" -> builder.append("return new BigDecimal(leftValue.toString()).divide(new BigDecimal(rightValue.toString()));");
                        case ">" -> builder.append("return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) > 0;");
                        case ">=" -> builder.append("return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) >= 0;");
                        case "<" -> builder.append("return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) < 0;");
                        case "<=" -> builder.append("return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) <= 0;");
                        case "==" -> builder.append("return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) == 0;");
                    }
                }
                if (leftValue instanceof Integer || leftValue instanceof Short || leftValue instanceof Byte)
                {
                    if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
                    {
                        builder.append(STR.format("return ((Number)a).intValue() {} ((Number)b).intValue();", mathOperator));
                    }
                    else if (rightValue instanceof Long b)
                    {
                        builder.append(STR.format("return ((Number)a).intValue() {} ((Number)b).longValue();", mathOperator));
                    }
                    else if (rightValue instanceof Float b)
                    {
                        builder.append(STR.format("return ((Number)a).intValue() {} ((Number)b).floatValue();", mathOperator));
                    }
                    else if (rightValue instanceof Double b)
                    {
                        builder.append(STR.format("return ((Number)a).intValue() {} ((Number)b).doubleValue();", mathOperator));
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
                        builder.append(STR.format("return ((Number)a).longValue() {} ((Number)b).intValue();", mathOperator));
                    }
                    else if (rightValue instanceof Long b)
                    {
                        builder.append(STR.format("return ((Number)a).longValue() {} ((Number)b).longValue();", mathOperator));
                    }
                    else if (rightValue instanceof Float b)
                    {
                        builder.append(STR.format("return ((Number)a).longValue() {} ((Number)b).floatValue();", mathOperator));
                    }
                    else if (rightValue instanceof Double b)
                    {
                        builder.append(STR.format("return ((Number)a).longValue() {} ((Number)b).doubleValue();", mathOperator));
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
                        builder.append(STR.format("return ((Number)a).floatValue() {} ((Number)b).intValue();", mathOperator));
                    }
                    else if (rightValue instanceof Long b)
                    {
                        builder.append(STR.format("return ((Number)a).floatValue() {} ((Number)b).longValue();", mathOperator));
                    }
                    else if (rightValue instanceof Float b)
                    {
                        builder.append(STR.format("return ((Number)a).floatValue() {} ((Number)b).floatValue();", mathOperator));
                    }
                    else if (rightValue instanceof Double b)
                    {
                        builder.append(STR.format("return ((Number)a).floatValue() {} ((Number)b).doubleValue();", mathOperator));
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
                        builder.append(STR.format("return ((Number)a).doubleValue() {} ((Number)b).intValue();", mathOperator));
                    }
                    else if (rightValue instanceof Long b)
                    {
                        builder.append(STR.format("return ((Number)a).doubleValue() {} ((Number)b).longValue();", mathOperator));
                    }
                    else if (rightValue instanceof Float b)
                    {
                        builder.append(STR.format("return ((Number)a).doubleValue() {} ((Number)b).floatValue();", mathOperator));
                    }
                    else if (rightValue instanceof Double b)
                    {
                        builder.append(STR.format("return ((Number)a).doubleValue() {} ((Number)b).doubleValue();", mathOperator));
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
            }
            else
            {
                switch (mathOperator)
                {
                    case "+" ->
                    {
                        if (leftValue instanceof String || rightValue instanceof String)
                        {
                            builder.append("""
                                                   StringBuilder builder = new Stringbuilder();
                                                   return builder.append(a).append(b).toString();""");
                        }
                        else
                        {
                            throw new IllegalArgumentException();
                        }
                    }
                    case "==" ->
                    {
                        builder.append("""
                                               if (a == null)
                                                                       {
                                                                           return b == null;
                                                                       }
                                                                       else
                                                                       {
                                                                           if (b == null)
                                                                           {
                                                                               return false;
                                                                           }
                                                                           else
                                                                           {
                                                                               return a.equals(b);
                                                                           }
                                                                       }""");
                    }
                    case "!=" ->
                    {
                        builder.append("""
                                               if (a == null)
                                                                       {
                                                                           return b != null;
                                                                       }
                                                                       else
                                                                       {
                                                                           if (b == null)
                                                                           {
                                                                               return false;
                                                                           }
                                                                           else
                                                                           {
                                                                               return !a.equals(b);
                                                                           }
                                                                       }""");
                    }
                    default -> throw new IllegalStateException(STR.format("操作数解析出现异常，{} 操作符要求左右参数都是 Number。异常解析位置为{}", mathOperator, fragment));
                }
            }
            methodModel.setBody(builder.toString());
            classModel.putMethodModel(methodModel);
            Class<BiFunction> compile        = (Class<BiFunction>) COMPILE_HELPER.compile(classModel);
            BiFunction        biFunction     = compile.getConstructor().newInstance();
            RuntimeOperand    runtimeOperand = new RuntimeOperand(left, right, biFunction);
            changeRuntimeOperand.newOperand(runtimeOperand);
            return biFunction.apply(leftValue, rightValue);
        }
    }

    @Data
    class RuntimeOperand implements Operand
    {
        final private Operand                            left;
        final private Operand                            right;
        final         BiFunction<Object, Object, Object> operand;

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue  = left.calculate(contextParam);
            Object rightValue = right.calculate(contextParam);
            return operand.apply(leftValue, rightValue);
        }
    }
}
