package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.Operand;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    protected abstract static class MathOperand extends BasicOperandImpl
    {
        public MathOperand(Operand left, Operand right, String fragment)
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
                if (leftValue instanceof BigDecimal || rightValue instanceof BigDecimal)
                {
                    return mathBigDecimal(leftValue, rightValue);
                }
                if (leftValue instanceof Byte || leftValue instanceof Short || leftValue instanceof Integer || leftValue instanceof Long)
                {
                    long l = ((Number) leftValue).longValue();
                    if (rightValue instanceof Byte || rightValue instanceof Short || rightValue instanceof Integer || rightValue instanceof Long)
                    {
                        return math(l, ((Number) rightValue).longValue());
                    }
                    else if (rightValue instanceof Float || rightValue instanceof Double)
                    {
                        return math(l, ((Number) rightValue).doubleValue());
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
                        return math(v, ((Number) rightValue).longValue());
                    }
                    else if (rightValue instanceof Float || rightValue instanceof Double)
                    {
                        return math(v, ((Number) rightValue).doubleValue());
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

        protected abstract Object mathBigDecimal(Object leftValue, Object rightValue);

        protected abstract Object math(double v, double v1);

        protected abstract Object math(double v, long l);

        protected abstract Object math(long l, double v);

        protected abstract Object math(long l, long l1);

        protected abstract Object processOther(Object leftValue, Object rightValue);
    }

    public static class PlusOperand extends MathOperand
    {
        private static final ThreadLocal<StringBuilder> BUILDER = ThreadLocal.withInitial(StringBuilder::new);

        public PlusOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object mathBigDecimal(Object leftValue, Object rightValue)
        {
            return new BigDecimal(leftValue.toString()).add(new BigDecimal(rightValue.toString()));
        }

        @Override
        protected Object math(double v, double v1)
        {
            return v + v1;
        }

        @Override
        protected Object math(double v, long l)
        {
            return v + l;
        }

        @Override
        protected Object math(long l, double v)
        {
            return l + v;
        }

        @Override
        protected Object math(long l, long l1)
        {
            return l + l1;
        }

        @Override
        protected Object processOther(Object leftValue, Object rightValue)
        {
            if (leftValue instanceof String || rightValue instanceof String)
            {
                StringBuilder builder = BUILDER.get();
                String        result  = builder.append(leftValue).append(rightValue).toString();
                builder.setLength(0);
                return result;
            }
            else
            {
                throw new IllegalArgumentException();
            }
        }
    }

    public static class MinusOperand extends MathOperand
    {
        public MinusOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object mathBigDecimal(Object leftValue, Object rightValue)
        {
            return new BigDecimal(leftValue.toString()).subtract(new BigDecimal(rightValue.toString()));
        }

        @Override
        protected Object math(double v, double v1)
        {
            return v - v1;
        }

        @Override
        protected Object math(double v, long l)
        {
            return v - l;
        }

        @Override
        protected Object math(long l, double v)
        {
            return l - v;
        }

        @Override
        protected Object math(long l, long l1)
        {
            return l - l1;
        }

        @Override
        protected Object processOther(Object leftValue, Object rightValue)
        {
            throw new IllegalStateException("操作数解析出现异常，- 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
        }
    }

    public static class TimesOperand extends MathOperand
    {
        public TimesOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object mathBigDecimal(Object leftValue, Object rightValue)
        {
            return new BigDecimal(leftValue.toString()).multiply(new BigDecimal(rightValue.toString()));
        }

        @Override
        protected Object math(double v, double v1)
        {
            return v * v1;
        }

        @Override
        protected Object math(double v, long l)
        {
            return v * l;
        }

        @Override
        protected Object math(long l, double v)
        {
            return l * v;
        }

        @Override
        protected Object math(long l, long l1)
        {
            return l * l1;
        }

        @Override
        protected Object processOther(Object leftValue, Object rightValue)
        {
            throw new IllegalStateException("操作数解析出现异常，* 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
        }
    }

    public static class DivisionOperand extends MathOperand
    {
        public DivisionOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object mathBigDecimal(Object leftValue, Object rightValue)
        {
            return new BigDecimal(leftValue.toString()).divide(new BigDecimal(rightValue.toString()), 2, RoundingMode.HALF_UP);
        }

        @Override
        protected Object math(double v, double v1)
        {
            return v / v1;
        }

        @Override
        protected Object math(double v, long l)
        {
            return v / l;
        }

        @Override
        protected Object math(long l, double v)
        {
            return l / v;
        }

        @Override
        protected Object math(long l, long l1)
        {
            return l / l1;
        }

        @Override
        protected Object processOther(Object leftValue, Object rightValue)
        {
            throw new IllegalStateException("操作数解析出现异常，/ 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
        }
    }

    public static class RemainOperand extends MathOperand
    {
        public RemainOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object mathBigDecimal(Object leftValue, Object rightValue)
        {
            return new BigDecimal(leftValue.toString()).remainder(new BigDecimal(rightValue.toString()));
        }

        @Override
        protected Object math(double v, double v1)
        {
            return v % v1;
        }

        @Override
        protected Object math(double v, long l)
        {
            return v % l;
        }

        @Override
        protected Object math(long l, double v)
        {
            return l % v;
        }

        @Override
        protected Object math(long l, long l1)
        {
            return l % l1;
        }

        @Override
        protected Object processOther(Object leftValue, Object rightValue)
        {
            throw new IllegalStateException("操作数解析出现异常，% 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
        }
    }

    public static class EqOperand extends MathOperand
    {
        public EqOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
            String msg = "操作数解析出现异常，= 操作符要求左右参数都是可比较对象。异常解析位置为" + fragment;
        }

        @Override
        protected Object mathBigDecimal(Object leftValue, Object rightValue)
        {
            return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) == 0;
        }

        @Override
        protected Object math(double v, double v1)
        {
            return v == v1;
        }

        @Override
        protected Object math(double v, long l)
        {
            return v == l;
        }

        @Override
        protected Object math(long l, double v)
        {
            return l == v;
        }

        @Override
        protected Object math(long l, long l1)
        {
            return l == l1;
        }

        @Override
        protected Object processOther(Object leftValue, Object rightValue)
        {
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

    public static class GeOperand extends MathOperand
    {
        public GeOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object mathBigDecimal(Object leftValue, Object rightValue)
        {
            return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) >= 0;
        }

        @Override
        protected Object math(double v, double v1)
        {
            return v >= v1;
        }

        @Override
        protected Object math(double v, long l)
        {
            return v >= l;
        }

        @Override
        protected Object math(long l, double v)
        {
            return l >= v;
        }

        @Override
        protected Object math(long l, long l1)
        {
            return l >= l1;
        }

        @Override
        protected Object processOther(Object leftValue, Object rightValue)
        {
            throw new IllegalStateException("操作数解析出现异常，>= 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
        }
    }

    public static class GtOperand extends MathOperand
    {
        public GtOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object mathBigDecimal(Object leftValue, Object rightValue)
        {
            return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) > 0;
        }

        @Override
        protected Object math(double v, double v1)
        {
            return v > v1;
        }

        @Override
        protected Object math(double v, long l)
        {
            return v > l;
        }

        @Override
        protected Object math(long l, double v)
        {
            return l > v;
        }

        @Override
        protected Object math(long l, long l1)
        {
            return l > l1;
        }

        @Override
        protected Object processOther(Object leftValue, Object rightValue)
        {
            throw new IllegalStateException("操作数解析出现异常，> 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
        }
    }

    public static class LeOperand extends MathOperand
    {
        public LeOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object mathBigDecimal(Object leftValue, Object rightValue)
        {
            return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) <= 0;
        }

        @Override
        protected Object math(double v, double v1)
        {
            return v <= v1;
        }

        @Override
        protected Object math(double v, long l)
        {
            return v <= l;
        }

        @Override
        protected Object math(long l, double v)
        {
            return l <= v;
        }

        @Override
        protected Object math(long l, long l1)
        {
            return l <= l1;
        }

        @Override
        protected Object processOther(Object leftValue, Object rightValue)
        {
            throw new IllegalStateException("操作数解析出现异常，<= 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
        }
    }

    public static class LtOperand extends MathOperand
    {
        public LtOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object mathBigDecimal(Object leftValue, Object rightValue)
        {
            return new BigDecimal(leftValue.toString()).compareTo(new BigDecimal(rightValue.toString())) < 0;
        }

        @Override
        protected Object math(double v, double v1)
        {
            return v < v1;
        }

        @Override
        protected Object math(double v, long l)
        {
            return v < l;
        }

        @Override
        protected Object math(long l, double v)
        {
            return l < v;
        }

        @Override
        protected Object math(long l, long l1)
        {
            return l < l1;
        }

        @Override
        protected Object processOther(Object leftValue, Object rightValue)
        {
            throw new IllegalStateException("操作数解析出现异常，< 操作符要求左右参数都是 Number。异常解析位置为" + fragment);
        }
    }
}
