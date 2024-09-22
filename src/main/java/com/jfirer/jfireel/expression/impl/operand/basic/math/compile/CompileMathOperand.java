package com.jfirer.jfireel.expression.impl.operand.basic.math.compile;

import com.jfirer.baseutil.STR;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.FieldModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.basic.math.ChangeRuntimeOperand;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.util.Map;

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
            classModel.addInterface(Operand.class);
            classModel.addField(new FieldModel("left", Operand.class, classModel), new FieldModel("right", Operand.class, classModel));
            classModel.addConstructor("this.left=$0;this.right=$1;", Operand.class, Operand.class);
            MethodModel methodModel = new MethodModel(Operand.class.getDeclaredMethod("calculate", Map.class), classModel);
            methodModel.setParamterNames("contextParam");
            StringBuilder builder        = new StringBuilder();
            Object        thisTimeResult = null;
            builder.append("""
                                   Object leftValue = left.calculate(contextParam);
                                   Object rightValue = right.calculate(contextParam);
                                   """);
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                if (leftValue instanceof BigDecimal || rightValue instanceof BigDecimal)
                {
                    switch (mathOperator)
                    {
                        case "-" ->
                        {
                            builder.append("return new BigDecimal(leftValue.toString()).subtract(new BigDecimal(rightValue.toString()));");
                            thisTimeResult = new BigDecimal(leftValue.toString()).subtract(new BigDecimal(rightValue.toString()));
                        }
                        case "+" ->
                        {
                            builder.append("return new BigDecimal(leftValue.toString()).add(new BigDecimal(rightValue.toString()));");
                            thisTimeResult = new BigDecimal(leftValue.toString()).add(new BigDecimal(rightValue.toString()));
                        }
                        case "*" ->
                        {
                            builder.append("return new BigDecimal(leftValue.toString()).multiply(new BigDecimal(rightValue.toString()));");
                            thisTimeResult = new BigDecimal(leftValue.toString()).multiply(new BigDecimal(rightValue.toString()));
                        }
                        case "/" ->
                        {
                            builder.append("return new BigDecimal(leftValue.toString()).divide(new BigDecimal(rightValue.toString()));");
                            thisTimeResult = new BigDecimal(leftValue.toString()).divide(new BigDecimal(rightValue.toString()));
                        }
                        case ">" ->
                        {
                            builder.append("return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) > 0;");
                            thisTimeResult = new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) > 0;
                        }
                        case ">=" ->
                        {
                            builder.append("return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) >= 0;");
                            thisTimeResult = new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) >= 0;
                        }
                        case "<" ->
                        {
                            builder.append("return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) < 0;");
                            thisTimeResult = new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) < 0;
                        }
                        case "<=" ->
                        {
                            builder.append("return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) <= 0;");
                            thisTimeResult = new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) <= 0;
                        }
                        case "==" ->
                        {
                            builder.append("return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) == 0;");
                            thisTimeResult = new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) == 0;
                        }
                        case "!=" ->
                        {
                            builder.append("return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) != 0;");
                            thisTimeResult = new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) != 0;
                        }
                        case "%" ->
                        {
                            builder.append("return new BigDecimal(leftValue.toString()).remainder(new BigDecimal(rightValue.toString()));");
                            thisTimeResult = new BigDecimal(leftValue.toString()).remainder(new BigDecimal(rightValue.toString()));
                        }
                    }
                }
                else
                {
                    if (leftValue instanceof Integer || leftValue instanceof Short || leftValue instanceof Byte)
                    {
                        if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
                        {
                            builder.append(STR.format("return ((Number)leftValue).intValue() {} ((Number)leftValue).intValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).intValue() - ((Number) rightValue).intValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).intValue() + ((Number) rightValue).intValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).intValue() * ((Number) rightValue).intValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).intValue() / ((Number) rightValue).intValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).intValue() > ((Number) rightValue).intValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).intValue() >= ((Number) rightValue).intValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).intValue() < ((Number) rightValue).intValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).intValue() <= ((Number) rightValue).intValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).intValue() == ((Number) rightValue).intValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).intValue() != ((Number) rightValue).intValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).intValue() % ((Number) rightValue).intValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else if (rightValue instanceof Long b)
                        {
                            builder.append(STR.format("return ((Number)leftValue).intValue() {} ((Number)leftValue).longValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).intValue() - ((Number) rightValue).longValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).intValue() + ((Number) rightValue).longValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).intValue() * ((Number) rightValue).longValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).intValue() / ((Number) rightValue).longValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).intValue() > ((Number) rightValue).longValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).intValue() >= ((Number) rightValue).longValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).intValue() < ((Number) rightValue).longValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).intValue() <= ((Number) rightValue).longValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).intValue() == ((Number) rightValue).longValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).intValue() != ((Number) rightValue).longValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).intValue() % ((Number) rightValue).longValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else if (rightValue instanceof Float b)
                        {
                            builder.append(STR.format("return ((Number)leftValue).intValue() {} ((Number)leftValue).floatValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).intValue() - ((Number) rightValue).floatValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).intValue() + ((Number) rightValue).floatValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).intValue() * ((Number) rightValue).floatValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).intValue() / ((Number) rightValue).floatValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).intValue() > ((Number) rightValue).floatValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).intValue() >= ((Number) rightValue).floatValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).intValue() < ((Number) rightValue).floatValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).intValue() <= ((Number) rightValue).floatValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).intValue() == ((Number) rightValue).floatValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).intValue() != ((Number) rightValue).floatValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).intValue() % ((Number) rightValue).floatValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else if (rightValue instanceof Double b)
                        {
                            builder.append(STR.format("return ((Number)leftValue).intValue() {} ((Number)leftValue).doubleValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).intValue() - ((Number) rightValue).doubleValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).intValue() + ((Number) rightValue).doubleValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).intValue() * ((Number) rightValue).doubleValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).intValue() / ((Number) rightValue).doubleValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).intValue() > ((Number) rightValue).doubleValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).intValue() >= ((Number) rightValue).doubleValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).intValue() < ((Number) rightValue).doubleValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).intValue() <= ((Number) rightValue).doubleValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).intValue() == ((Number) rightValue).doubleValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).intValue() != ((Number) rightValue).doubleValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).intValue() % ((Number) rightValue).doubleValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else
                        {
                            throw new IllegalArgumentException();
                        }
                    }
                    else if (leftValue instanceof Long)
                    {
                        if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
                        {
                            builder.append(STR.format("return ((Number)leftValue).longValue() {} ((Number)leftValue).intValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).longValue() - ((Number) rightValue).intValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).longValue() + ((Number) rightValue).intValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).longValue() * ((Number) rightValue).intValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).longValue() / ((Number) rightValue).intValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).longValue() > ((Number) rightValue).intValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).longValue() >= ((Number) rightValue).intValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).longValue() < ((Number) rightValue).intValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).longValue() <= ((Number) rightValue).intValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).longValue() == ((Number) rightValue).intValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).longValue() != ((Number) rightValue).intValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).longValue() % ((Number) rightValue).intValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else if (rightValue instanceof Long b)
                        {
                            builder.append(STR.format("return ((Number)leftValue).longValue() {} ((Number)leftValue).longValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).longValue() - ((Number) rightValue).longValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).longValue() + ((Number) rightValue).longValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).longValue() * ((Number) rightValue).longValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).longValue() / ((Number) rightValue).longValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).longValue() > ((Number) rightValue).longValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).longValue() >= ((Number) rightValue).longValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).longValue() < ((Number) rightValue).longValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).longValue() <= ((Number) rightValue).longValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).longValue() == ((Number) rightValue).longValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).longValue() != ((Number) rightValue).longValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).longValue() % ((Number) rightValue).longValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else if (rightValue instanceof Float b)
                        {
                            builder.append(STR.format("return ((Number)leftValue).longValue() {} ((Number)leftValue).floatValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).longValue() - ((Number) rightValue).floatValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).longValue() + ((Number) rightValue).floatValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).longValue() * ((Number) rightValue).floatValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).longValue() / ((Number) rightValue).floatValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).longValue() > ((Number) rightValue).floatValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).longValue() >= ((Number) rightValue).floatValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).longValue() < ((Number) rightValue).floatValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).longValue() <= ((Number) rightValue).floatValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).longValue() == ((Number) rightValue).floatValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).longValue() != ((Number) rightValue).floatValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).longValue() % ((Number) rightValue).floatValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else if (rightValue instanceof Double b)
                        {
                            builder.append(STR.format("return ((Number)leftValue).longValue() {} ((Number)leftValue).doubleValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).longValue() - ((Number) rightValue).doubleValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).longValue() + ((Number) rightValue).doubleValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).longValue() * ((Number) rightValue).doubleValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).longValue() / ((Number) rightValue).doubleValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).longValue() > ((Number) rightValue).doubleValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).longValue() >= ((Number) rightValue).doubleValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).longValue() < ((Number) rightValue).doubleValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).longValue() <= ((Number) rightValue).doubleValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).longValue() == ((Number) rightValue).doubleValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).longValue() != ((Number) rightValue).doubleValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).longValue() % ((Number) rightValue).doubleValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else
                        {
                            throw new IllegalArgumentException();
                        }
                    }
                    else if (leftValue instanceof Float)
                    {
                        if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
                        {
                            builder.append(STR.format("return ((Number)leftValue).floatValue() {} ((Number)leftValue).intValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).floatValue() - ((Number) rightValue).intValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).floatValue() + ((Number) rightValue).intValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).floatValue() * ((Number) rightValue).intValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).floatValue() / ((Number) rightValue).intValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).floatValue() > ((Number) rightValue).intValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).floatValue() >= ((Number) rightValue).intValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).floatValue() < ((Number) rightValue).intValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).floatValue() <= ((Number) rightValue).intValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).floatValue() == ((Number) rightValue).intValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).floatValue() != ((Number) rightValue).intValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).floatValue() % ((Number) rightValue).intValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else if (rightValue instanceof Long)
                        {
                            builder.append(STR.format("return ((Number)leftValue).floatValue() {} ((Number)leftValue).longValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).floatValue() - ((Number) rightValue).longValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).floatValue() + ((Number) rightValue).longValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).floatValue() * ((Number) rightValue).longValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).floatValue() / ((Number) rightValue).longValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).floatValue() > ((Number) rightValue).longValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).floatValue() >= ((Number) rightValue).longValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).floatValue() < ((Number) rightValue).longValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).floatValue() <= ((Number) rightValue).longValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).floatValue() == ((Number) rightValue).longValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).floatValue() != ((Number) rightValue).longValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).floatValue() % ((Number) rightValue).longValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else if (rightValue instanceof Float)
                        {
                            builder.append(STR.format("return ((Number)leftValue).floatValue() {} ((Number)leftValue).floatValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).floatValue() - ((Number) rightValue).floatValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).floatValue() + ((Number) rightValue).floatValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).floatValue() * ((Number) rightValue).floatValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).floatValue() / ((Number) rightValue).floatValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).floatValue() > ((Number) rightValue).floatValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).floatValue() >= ((Number) rightValue).floatValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).floatValue() < ((Number) rightValue).floatValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).floatValue() <= ((Number) rightValue).floatValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).floatValue() == ((Number) rightValue).floatValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).floatValue() != ((Number) rightValue).floatValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).floatValue() % ((Number) rightValue).floatValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else if (rightValue instanceof Double)
                        {
                            builder.append(STR.format("return ((Number)leftValue).floatValue() {} ((Number)leftValue).doubleValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).floatValue() - ((Number) rightValue).doubleValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).floatValue() + ((Number) rightValue).doubleValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).floatValue() * ((Number) rightValue).doubleValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).floatValue() / ((Number) rightValue).doubleValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).floatValue() > ((Number) rightValue).doubleValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).floatValue() >= ((Number) rightValue).doubleValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).floatValue() < ((Number) rightValue).doubleValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).floatValue() <= ((Number) rightValue).doubleValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).floatValue() == ((Number) rightValue).doubleValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).floatValue() != ((Number) rightValue).doubleValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).floatValue() % ((Number) rightValue).doubleValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else
                        {
                            throw new IllegalArgumentException();
                        }
                    }
                    else if (leftValue instanceof Double)
                    {
                        if (rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
                        {
                            builder.append(STR.format("return ((Number)leftValue).doubleValue() {} ((Number)leftValue).intValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).doubleValue() - ((Number) rightValue).intValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).doubleValue() + ((Number) rightValue).intValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).doubleValue() * ((Number) rightValue).intValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).doubleValue() / ((Number) rightValue).intValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).doubleValue() > ((Number) rightValue).intValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).doubleValue() >= ((Number) rightValue).intValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).doubleValue() < ((Number) rightValue).intValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).doubleValue() <= ((Number) rightValue).intValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).doubleValue() == ((Number) rightValue).intValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).doubleValue() != ((Number) rightValue).intValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).doubleValue() % ((Number) rightValue).intValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else if (rightValue instanceof Long)
                        {
                            builder.append(STR.format("return ((Number)leftValue).doubleValue() {} ((Number)leftValue).longValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).doubleValue() - ((Number) rightValue).longValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).doubleValue() + ((Number) rightValue).longValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).doubleValue() * ((Number) rightValue).longValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).doubleValue() / ((Number) rightValue).longValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).doubleValue() > ((Number) rightValue).longValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).doubleValue() >= ((Number) rightValue).longValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).doubleValue() < ((Number) rightValue).longValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).doubleValue() <= ((Number) rightValue).longValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).doubleValue() == ((Number) rightValue).longValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).doubleValue() != ((Number) rightValue).longValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).doubleValue() % ((Number) rightValue).longValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else if (rightValue instanceof Float)
                        {
                            builder.append(STR.format("return ((Number)leftValue).doubleValue() {} ((Number)leftValue).floatValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).doubleValue() - ((Number) rightValue).floatValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).doubleValue() + ((Number) rightValue).floatValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).doubleValue() * ((Number) rightValue).floatValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).doubleValue() / ((Number) rightValue).floatValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).doubleValue() > ((Number) rightValue).floatValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).doubleValue() >= ((Number) rightValue).floatValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).doubleValue() < ((Number) rightValue).floatValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).doubleValue() <= ((Number) rightValue).floatValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).doubleValue() == ((Number) rightValue).floatValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).doubleValue() != ((Number) rightValue).floatValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).doubleValue() % ((Number) rightValue).floatValue();
                                default -> throw new IllegalArgumentException();
                            }
                        }
                        else if (rightValue instanceof Double)
                        {
                            builder.append(STR.format("return ((Number)leftValue).doubleValue() {} ((Number)leftValue).doubleValue();", mathOperator));
                            switch (mathOperator)
                            {
                                case "-" -> thisTimeResult = ((Number) leftValue).doubleValue() - ((Number) rightValue).doubleValue();
                                case "+" -> thisTimeResult = ((Number) leftValue).doubleValue() + ((Number) rightValue).doubleValue();
                                case "*" -> thisTimeResult = ((Number) leftValue).doubleValue() * ((Number) rightValue).doubleValue();
                                case "/" -> thisTimeResult = ((Number) leftValue).doubleValue() / ((Number) rightValue).doubleValue();
                                case ">" -> thisTimeResult = ((Number) leftValue).doubleValue() > ((Number) rightValue).doubleValue();
                                case ">=" -> thisTimeResult = ((Number) leftValue).doubleValue() >= ((Number) rightValue).doubleValue();
                                case "<" -> thisTimeResult = ((Number) leftValue).doubleValue() < ((Number) rightValue).doubleValue();
                                case "<=" -> thisTimeResult = ((Number) leftValue).doubleValue() <= ((Number) rightValue).doubleValue();
                                case "==" -> thisTimeResult = ((Number) leftValue).doubleValue() == ((Number) rightValue).doubleValue();
                                case "!=" -> thisTimeResult = ((Number) leftValue).doubleValue() != ((Number) rightValue).doubleValue();
                                case "%" -> thisTimeResult = ((Number) leftValue).doubleValue() % ((Number) rightValue).doubleValue();
                                default -> throw new IllegalArgumentException();
                            }
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
                                                   return builder.append(leftValue).append(rightValue).toString();""");
                        }
                        else
                        {
                            throw new IllegalArgumentException();
                        }
                    }
                    case "==" ->
                    {
                        builder.append("""
                                               if (leftValue == null)
                                                                       {
                                                                           return rightValue == null;
                                                                       }
                                                                       else
                                                                       {
                                                                           if (rightValue == null)
                                                                           {
                                                                               return false;
                                                                           }
                                                                           else
                                                                           {
                                                                               return leftValue.equals(rightValue);
                                                                           }
                                                                       }""");
                    }
                    case "!=" ->
                    {
                        builder.append("""
                                               if (leftValue == null)
                                                                       {
                                                                           return rightValue != null;
                                                                       }
                                                                       else
                                                                       {
                                                                           if (rightValue == null)
                                                                           {
                                                                               return false;
                                                                           }
                                                                           else
                                                                           {
                                                                               return !leftValue.equals(rightValue);
                                                                           }
                                                                       }""");
                    }
                    default -> throw new IllegalStateException(STR.format("操作数解析出现异常，{} 操作符要求左右参数都是 Number。异常解析位置为{}", mathOperator, fragment));
                }
            }
            methodModel.setBody(builder.toString());
            classModel.putMethodModel(methodModel);
            Class<Operand> compile = (Class<Operand>) COMPILE_HELPER.compile(classModel);
            Operand        operand = compile.getConstructor(Operand.class, Operand.class).newInstance(left, right);
            changeRuntimeOperand.newOperand(operand);
            return thisTimeResult;
        }
    }
}
