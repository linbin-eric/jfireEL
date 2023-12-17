package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;

import java.util.Map;

public abstract class BasicOperandImpl implements Operand
{
    protected Operand left;
    protected Operand right;
    protected String  fragment;

    public BasicOperandImpl(Operand left, Operand right, String fragment)
    {
        this.left     = left;
        this.right    = right;
        this.fragment = fragment;
    }

    public static class AndOperand extends BasicOperandImpl
    {
        public AndOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue = left.calculate(contextParam);
            if (leftValue instanceof Boolean b)
            {
                if (!b.booleanValue())
                {
                    return false;
                }
                else
                {
                    Object rightValue = right.calculate(contextParam);
                    if (rightValue instanceof Boolean b2)
                    {
                        return b2.booleanValue();
                    }
                    else
                    {
                        throw new IllegalStateException("操作数解析出现异常，&& 操作符要求左右参数都是 Boolean。异常解析位置为" + fragment);
                    }
                }
            }
            else
            {
                throw new IllegalStateException("操作数解析出现异常，&& 操作符要求左右参数都是 Boolean。异常解析位置为" + fragment);
            }
        }
    }

    public static class OrOperand extends BasicOperandImpl
    {
        public OrOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue = left.calculate(contextParam);
            if (leftValue instanceof Boolean b)
            {
                if (b.booleanValue())
                {
                    return true;
                }
                else
                {
                    Object rightValue = right.calculate(contextParam);
                    if (rightValue instanceof Boolean b2)
                    {
                        return b2.booleanValue();
                    }
                    else
                    {
                        throw new IllegalStateException("操作数解析出现异常，|| 操作符要求左右参数都是 Boolean。异常解析位置为" + fragment);
                    }
                }
            }
            else
            {
                throw new IllegalStateException("操作数解析出现异常，|| 操作符要求左右参数都是 Boolean。异常解析位置为" + fragment);
            }
        }
    }

    interface MathOp1
    {
        Object math(long l, long r);
    }

    interface MathOp2
    {
        Object math(double l, double r);
    }

    interface MathOp3
    {
        Object math(long l, double r);
    }

    interface MathOp4
    {
        Object math(double l, long r);
    }

    protected abstract class MathOperand extends BasicOperandImpl
    {
        MathOp1 op1;
        MathOp2 op2;
        MathOp3 op3;
        MathOp4 op4;

        public MathOperand(Operand left, Operand right, String fragment, MathOp1 mathOp1, MathOp2 mathOp2, MathOp3 mathOp3, MathOp4 mathOp4)
        {
            super(left, right, fragment);
            this.op1 = mathOp1;
            this.op2 = mathOp2;
            this.op3 = mathOp3;
            this.op4 = mathOp4;
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue  = left.calculate(contextParam);
            Object rightValue = right.calculate(contextParam);
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                if (leftValue instanceof Byte || leftValue instanceof Short || leftValue instanceof Integer || leftValue instanceof Long)
                {
                    long l = ((Number) leftValue).longValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer || rightValue instanceof Long)
                    {
                        return op1.math(l, ((Number) rightValue).longValue());
                    }
                    else if (rightValue instanceof Float || rightValue instanceof Double)
                    {
                        return op3.math(l, ((Number) rightValue).doubleValue());
                    }
                    else
                    {
                        throw new IllegalArgumentException();
                    }
                }
                else if (leftValue instanceof Float || leftValue instanceof Double)
                {
                    double v = ((Number) leftValue).doubleValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer || rightValue instanceof Long)
                    {
                        return op4.math(v, ((Number) rightValue).longValue());
                    }
                    else if (rightValue instanceof Float || rightValue instanceof Double)
                    {
                        return op2.math(v, ((Number) rightValue).doubleValue());
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
                return processOther(leftValue, rightValue);
            }
        }

        protected abstract Object processOther(Object leftValue, Object rightValue);
    }

    public static class PlusOperand extends BasicOperandImpl
    {
        private static final ThreadLocal<StringBuilder> BUILDER = ThreadLocal.withInitial(() -> new StringBuilder());

        public PlusOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue  = left.calculate(contextParam);
            Object rightValue = right.calculate(contextParam);
            if (leftValue instanceof String || rightValue instanceof String)
            {
                StringBuilder builder = BUILDER.get();
                String        result  = builder.append(leftValue).append(rightValue).toString();
                builder.setLength(0);
                return result;
            }
            else if (leftValue instanceof Number && rightValue instanceof Number)
            {
                if (leftValue instanceof Byte || leftValue instanceof Short || leftValue instanceof Integer || leftValue instanceof Long)
                {
                    long i = ((Number) leftValue).longValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer || rightValue instanceof Long)
                    {
                        return i + ((Number) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float || rightValue instanceof Double)
                    {
                        return i + ((Number) rightValue).doubleValue();
                    }
                    else
                    {
                        throw new IllegalArgumentException();
                    }
                }
                else if (leftValue instanceof Float || leftValue instanceof Double)
                {
                    double i = ((Number) leftValue).doubleValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer || rightValue instanceof Long)
                    {
                        return i + ((Number) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float || rightValue instanceof Double)
                    {
                        return i + ((Number) rightValue).doubleValue();
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
                throw new IllegalStateException("操作数解析出现异常，+ 操作符要求左右参数都是 String,Number。异常解析位置为" + fragment);
            }
        }
    }

    public static class MinusOperand extends BasicOperandImpl
    {
        public MinusOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue  = left.calculate(contextParam);
            Object rightValue = right.calculate(contextParam);
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                if (leftValue instanceof Byte || leftValue instanceof Short || leftValue instanceof Integer || leftValue instanceof Long)
                {
                    long i = ((Number) leftValue).longValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer || rightValue instanceof Long)
                    {
                        return i - ((Number) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float || rightValue instanceof Double)
                    {
                        return i - ((Number) rightValue).doubleValue();
                    }
                    else
                    {
                        throw new IllegalArgumentException();
                    }
                }
                else if (leftValue instanceof Float || leftValue instanceof Double)
                {
                    double i = ((Number) leftValue).doubleValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer || rightValue instanceof Long)
                    {
                        return i - ((Number) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float || rightValue instanceof Double)
                    {
                        return i - ((Number) rightValue).doubleValue();
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
                throw new IllegalStateException("操作数解析出现异常，- 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
            }
        }
    }

    public static class TimesOperand extends BasicOperandImpl
    {
        public TimesOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue  = left.calculate(contextParam);
            Object rightValue = right.calculate(contextParam);
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                if (leftValue instanceof Byte || leftValue instanceof Short || leftValue instanceof Integer)
                {
                    int i = ((Number) leftValue).intValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i * ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i * ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i * ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i * ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Long)
                {
                    long i = ((Number) leftValue).longValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i * ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i * ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i * ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i * ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Float)
                {
                    float i = ((Number) leftValue).floatValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i * ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i * ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i * ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i * ((Number) rightValue).doubleValue();
                    }
                }
                else
                {
                    double i = ((Number) leftValue).doubleValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i * ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i * ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i * ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i * ((Number) rightValue).doubleValue();
                    }
                }
            }
            else
            {
                throw new IllegalStateException("操作数解析出现异常，* 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
            }
        }
    }

    public static class DivisionOperand extends BasicOperandImpl
    {
        public DivisionOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue  = left.calculate(contextParam);
            Object rightValue = right.calculate(contextParam);
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                if (leftValue instanceof Byte || leftValue instanceof Short || leftValue instanceof Integer)
                {
                    int i = ((Number) leftValue).intValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i / ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i / ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i / ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i / ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Long)
                {
                    long i = ((Number) leftValue).longValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i / ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i / ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i / ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i / ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Float)
                {
                    float i = ((Number) leftValue).floatValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i / ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i / ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i / ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i / ((Number) rightValue).doubleValue();
                    }
                }
                else
                {
                    double i = ((Number) leftValue).doubleValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i / ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i / ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i / ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i / ((Number) rightValue).doubleValue();
                    }
                }
            }
            else
            {
                throw new IllegalStateException("操作数解析出现异常，/ 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
            }
        }
    }

    public static class RemainOperand extends BasicOperandImpl
    {
        public RemainOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue  = left.calculate(contextParam);
            Object rightValue = right.calculate(contextParam);
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                if (leftValue instanceof Byte || leftValue instanceof Short || leftValue instanceof Integer)
                {
                    int i = ((Number) leftValue).intValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i % ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i % ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i % ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i % ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Long)
                {
                    long i = ((Number) leftValue).longValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i % ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i % ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i % ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i % ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Float)
                {
                    float i = ((Number) leftValue).floatValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i % ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i % ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i % ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i % ((Number) rightValue).doubleValue();
                    }
                }
                else
                {
                    double i = ((Number) leftValue).doubleValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i % ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i % ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i % ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i % ((Number) rightValue).doubleValue();
                    }
                }
            }
            else
            {
                throw new IllegalStateException("操作数解析出现异常，% 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
            }
        }
    }

    public static class EqOperand extends BasicOperandImpl
    {
        public EqOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
            String msg = "操作数解析出现异常，= 操作符要求左右参数都是可比较对象。异常解析位置为" + fragment;
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue  = left.calculate(contextParam);
            Object rightValue = right.calculate(contextParam);
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                if (leftValue instanceof Byte || leftValue instanceof Short || leftValue instanceof Integer)
                {
                    int i = ((Number) leftValue).intValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i == ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i == ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i == ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i == ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Long)
                {
                    long i = ((Number) leftValue).longValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i == ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i == ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i == ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i == ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Float)
                {
                    float i = ((Number) leftValue).floatValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i == ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i == ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i == ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i == ((Number) rightValue).doubleValue();
                    }
                }
                else
                {
                    double i = ((Number) leftValue).doubleValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i == ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i == ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i == ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i == ((Number) rightValue).doubleValue();
                    }
                }
            }
            else if (leftValue == null)
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
            }
        }
    }

    public static class NotEqOperand extends BasicOperandImpl
    {
        private EqOperand eqOperand;

        public NotEqOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
            eqOperand = new EqOperand(left, right, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            return !(Boolean) eqOperand.calculate(contextParam);
        }
    }

    public static class GeOperand extends BasicOperandImpl
    {
        public GeOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue  = left.calculate(contextParam);
            Object rightValue = right.calculate(contextParam);
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                if (leftValue instanceof Byte || leftValue instanceof Short || leftValue instanceof Integer)
                {
                    int i = ((Number) leftValue).intValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i >= ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i >= ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i >= ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i >= ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Long)
                {
                    long i = ((Number) leftValue).longValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i >= ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i >= ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i >= ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i >= ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Float)
                {
                    float i = ((Number) leftValue).floatValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i >= ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i >= ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i >= ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i >= ((Number) rightValue).doubleValue();
                    }
                }
                else
                {
                    double i = ((Number) leftValue).doubleValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i >= ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i >= ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i >= ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i >= ((Number) rightValue).doubleValue();
                    }
                }
            }
            else
            {
                throw new IllegalStateException("操作数解析出现异常，>= 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
            }
        }
    }

    public static class GtOperand extends BasicOperandImpl
    {
        public GtOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue  = left.calculate(contextParam);
            Object rightValue = right.calculate(contextParam);
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                if (leftValue instanceof Byte || leftValue instanceof Short || leftValue instanceof Integer)
                {
                    int i = ((Number) leftValue).intValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i > ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i > ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i > ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i > ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Long)
                {
                    long i = ((Number) leftValue).longValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i > ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i > ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i > ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i > ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Float)
                {
                    float i = ((Number) leftValue).floatValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i > ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i > ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i > ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i > ((Number) rightValue).doubleValue();
                    }
                }
                else
                {
                    double i = ((Number) leftValue).doubleValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i > ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i > ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i > ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i > ((Number) rightValue).doubleValue();
                    }
                }
            }
            else
            {
                throw new IllegalStateException("操作数解析出现异常，> 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
            }
        }
    }

    public static class LeOperand extends BasicOperandImpl
    {
        public LeOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue  = left.calculate(contextParam);
            Object rightValue = right.calculate(contextParam);
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                if (leftValue instanceof Byte || leftValue instanceof Short || leftValue instanceof Integer)
                {
                    int i = ((Number) leftValue).intValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i <= ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i <= ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i <= ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i <= ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Long)
                {
                    long i = ((Number) leftValue).longValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i <= ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i <= ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i <= ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i <= ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Float)
                {
                    float i = ((Number) leftValue).floatValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i <= ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i <= ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i <= ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i <= ((Number) rightValue).doubleValue();
                    }
                }
                else
                {
                    double i = ((Number) leftValue).doubleValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i <= ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i <= ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i <= ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i <= ((Number) rightValue).doubleValue();
                    }
                }
            }
            else
            {
                throw new IllegalStateException("操作数解析出现异常，<= 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
            }
        }
    }

    public static class LtOperand extends BasicOperandImpl
    {
        public LtOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object leftValue  = left.calculate(contextParam);
            Object rightValue = right.calculate(contextParam);
            if (leftValue instanceof Number && rightValue instanceof Number)
            {
                if (leftValue instanceof Byte || leftValue instanceof Short || leftValue instanceof Integer)
                {
                    int i = ((Number) leftValue).intValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i < ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i < ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i < ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i < ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Long)
                {
                    long i = ((Number) leftValue).longValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i < ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i < ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i < ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i < ((Number) rightValue).doubleValue();
                    }
                }
                else if (leftValue instanceof Float)
                {
                    float i = ((Number) leftValue).floatValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i < ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i < ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i < ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i < ((Number) rightValue).doubleValue();
                    }
                }
                else
                {
                    double i = ((Number) leftValue).doubleValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer)
                    {
                        return i < ((Number) rightValue).intValue();
                    }
                    else if (rightValue instanceof Long)
                    {
                        return i < ((Long) rightValue).longValue();
                    }
                    else if (rightValue instanceof Float)
                    {
                        return i < ((Float) rightValue).floatValue();
                    }
                    else
                    {
                        return i < ((Number) rightValue).doubleValue();
                    }
                }
            }
            else
            {
                throw new IllegalStateException("操作数解析出现异常，< 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
            }
        }
    }
}
