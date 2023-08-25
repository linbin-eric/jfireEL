package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class BasicOperandImpl implements Operand
{
    protected Operand                             left;
    protected Operand                             right;
    protected String                              fragment;
    protected BiFunction<String, Object, Object>  stringObjectFunction;
    protected BiFunction<Number, Object, Object>  numberObjectFunction;
    protected BiFunction<Boolean, Object, Object> booleanOperandFunction;
    protected Function<Object, Object>            nullFunction;

    public BasicOperandImpl(Operand left, Operand right, String fragment)
    {
        this.left     = left;
        this.right    = right;
        this.fragment = fragment;
    }

    @Override
    public Object calculate(Map<String, Object> param)
    {
        Object leftValue  = left.calculate(param);
        Object rightValue = right.calculate(param);
//        if (leftValue == null)
//        {
//            if (rightValue == null)
//            {
//                return true;
//            }
//            else if (rightValue instanceof String s2)
//            {
//                return process(null, s2, param);
//            }
//            else if (rightValue instanceof Number)
//            {
//                if (rightValue instanceof Long || rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
//                {
//                    return process(s1, ((Number) rightValue).longValue(), param);
//                }
//                else if (rightValue instanceof Double || rightValue instanceof Float)
//                {
//                    return process(s1, ((Number) rightValue).doubleValue(), param);
//                }
//                else
//                {
//                    throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
//                }
//            }
//            else if (rightValue instanceof Boolean)
//            {
//                return process(s1, ((Boolean) rightValue), param);
//            }
//            else
//            {
//                throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
//            }
//        }
//        if (leftValue instanceof String s1)
//        {
//            if (rightValue == null)
//            {
//                return s1 == null;
//            }
//            else if (rightValue instanceof String s2)
//            {
//                return process(s1, s2, param);
//            }
//            else if (rightValue instanceof Number)
//            {
//                if (rightValue instanceof Long || rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
//                {
//                    return process(s1, ((Number) rightValue).longValue(), param);
//                }
//                else if (rightValue instanceof Double || rightValue instanceof Float)
//                {
//                    return process(s1, ((Number) rightValue).doubleValue(), param);
//                }
//                else
//                {
//                    throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
//                }
//            }
//            else if (rightValue instanceof Boolean)
//            {
//                return process(s1, ((Boolean) rightValue), param);
//            }
//            else
//            {
//                throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
//            }
//        }
//        else if (leftValue instanceof Number)
//        {
//            if (rightValue == null)
//            {
//                return leftValue == null;
//            }
//            else if (leftValue instanceof Integer || leftValue instanceof Long || leftValue instanceof Short || leftValue instanceof Byte)
//            {
//                long l1 = ((Number) leftValue).longValue();
//                if (rightValue instanceof String s2)
//                {
//                    return process(l1, s2, param);
//                }
//                else if (rightValue instanceof Number)
//                {
//                    if (rightValue instanceof Long || rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
//                    {
//                        return process(l1, ((Number) rightValue).longValue(), param);
//                    }
//                    else if (rightValue instanceof Double || rightValue instanceof Float)
//                    {
//                        return process(l1, ((Number) rightValue).doubleValue(), param);
//                    }
//                    else
//                    {
//                        throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
//                    }
//                }
//                else if (rightValue instanceof Boolean)
//                {
//                    return process(l1, ((Boolean) rightValue), param);
//                }
//                else
//                {
//                    throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
//                }
//            }
//            else
//            {
//                double d1 = ((Number) leftValue).doubleValue();
//                if (rightValue instanceof String s2)
//                {
//                    return process(d1, s2, param);
//                }
//                else if (rightValue instanceof Number)
//                {
//                    if (rightValue instanceof Long || rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
//                    {
//                        return process(d1, ((Number) rightValue).longValue(), param);
//                    }
//                    else if (rightValue instanceof Double || rightValue instanceof Float)
//                    {
//                        return process(d1, ((Number) rightValue).doubleValue(), param);
//                    }
//                    else
//                    {
//                        throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
//                    }
//                }
//                else if (rightValue instanceof Boolean)
//                {
//                    return process(d1, ((Boolean) rightValue), param);
//                }
//                else
//                {
//                    throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
//                }
//            }
//        }
//        else if (leftValue instanceof Boolean b1)
//        {
//            if (rightValue == null)
//            {
//                return leftValue == null;
//            }
//            else if (rightValue instanceof String s2)
//            {
//                return process(b1, s2, param);
//            }
//            else if (rightValue instanceof Number)
//            {
//                if (rightValue instanceof Long || rightValue instanceof Integer || rightValue instanceof Short || rightValue instanceof Byte)
//                {
//                    return process(b1, ((Number) rightValue).longValue(), param);
//                }
//                else if (rightValue instanceof Double || rightValue instanceof Float)
//                {
//                    return process(b1, ((Number) rightValue).doubleValue(), param);
//                }
//                else
//                {
//                    throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
//                }
//            }
//            else if (rightValue instanceof Boolean)
//            {
//                return process(b1, ((Boolean) rightValue), param);
//            }
//            else
//            {
//                throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
//            }
//        }
//        else
//        {
//            throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
//        }
        if (leftValue instanceof String s)
        {
            return stringObjectFunction.apply(s, rightValue);
        }
        else if (rightValue instanceof Number n)
        {
            return numberObjectFunction.apply(n, rightValue);
        }
        else if (rightValue instanceof Boolean b)
        {
            return booleanOperandFunction.apply(b, rightValue);
        }
        else
        {
            return nullFunction.apply(rightValue);
        }
    }

    protected Object process(Double d1, Double d2, Map<String, Object> param)   {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Double d1, Long l2, Map<String, Object> param)     {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Double d1, String s2, Map<String, Object> param)   {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Double d1, Boolean b2, Map<String, Object> param)  {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Long l1, Double d2, Map<String, Object> param)     {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Long l1, Long l2, Map<String, Object> param)       {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Long l1, String s2, Map<String, Object> param)     {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Long l1, Boolean b2, Map<String, Object> param)    {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(String s1, Double d2, Map<String, Object> param)   {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(String s1, Long l2, Map<String, Object> param)     {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(String s1, String s2, Map<String, Object> param)   {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(String s1, Boolean b2, Map<String, Object> param)  {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Boolean b1, Double d2, Map<String, Object> param)  {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Boolean b1, Long l2, Map<String, Object> param)    {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Boolean b1, String s2, Map<String, Object> param)  {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Boolean b1, Boolean b2, Map<String, Object> param) {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    public static class AndOperand extends BasicOperandImpl
    {
        public AndOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
            String msg = "操作数解析出现异常，&& 操作符要求左右参数都是 Boolean。异常解析位置为" + fragment;
            booleanOperandFunction = (b, o) -> {
                if (o instanceof Boolean b2)
                {
                    return b && b2;
                }
                else
                {
                    throw new IllegalArgumentException(msg);
                }
            };
            stringObjectFunction   = (s1, s2) -> {
                throw new IllegalArgumentException(msg);
            };
            numberObjectFunction   = (n1, n2) -> {
                throw new IllegalArgumentException(msg);
            };
            nullFunction           = n -> {
                throw new IllegalArgumentException(msg);
            };
        }

        @Override
        protected Object process(Boolean b1, Boolean b2, Map<String, Object> param)
        {
            return b1 && b2;
        }
    }

    public static class OrOperand extends BasicOperandImpl
    {
        public OrOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
            String msg = "操作数解析出现异常，|| 操作符要求左右参数都是 Boolean。异常解析位置为" + fragment;
            booleanOperandFunction = (b, o) -> {
                if (o instanceof Boolean b2)
                {
                    return b || b2;
                }
                else
                {
                    throw new IllegalArgumentException(msg);
                }
            };
            stringObjectFunction   = (s1, s2) -> {
                throw new IllegalArgumentException(msg);
            };
            numberObjectFunction   = (n1, n2) -> {
                throw new IllegalArgumentException(msg);
            };
            nullFunction           = n -> {
                throw new IllegalArgumentException(msg);
            };
        }

    }

    public static class PlusOperand extends BasicOperandImpl
    {
        public PlusOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
            String msg = "操作数解析出现异常，++ 操作符要求左右参数都是 String,Number。异常解析位置为" + fragment;
            booleanOperandFunction = (b, o) -> {
                if (o instanceof Boolean b2)
                {
                    return b && b2;
                }
                else
                {
                    throw new IllegalArgumentException(msg);
                }
            };
            stringObjectFunction   = (s1, s2) -> {
                throw new IllegalArgumentException(msg);
            };
            numberObjectFunction   = (n1, n2) -> {
                throw new IllegalArgumentException(msg);
            };
            nullFunction           = n -> {
                throw new IllegalArgumentException(msg);
            };
        }

        @Override
        protected Object process(String s1, String s2, Map<String, Object> param)
        {
            return s1 + s2;
        }

        @Override
        protected Object process(String s1, Long l2, Map<String, Object> param)
        {
            return s1 + l2;
        }

        @Override
        protected Object process(String s1, Double d2, Map<String, Object> param)
        {
            return s1 + d2;
        }

        @Override
        protected Object process(String s1, Boolean b2, Map<String, Object> param)
        {
            return s1 + b2;
        }

        @Override
        protected Object process(Long l1, Long l2, Map<String, Object> param)
        {
            return l1 + l2;
        }

        @Override
        protected Object process(Long l1, Double d2, Map<String, Object> param)
        {
            return l1 + d2;
        }

        @Override
        protected Object process(Long l1, String s2, Map<String, Object> param)
        {
            return l1 + s2;
        }

        @Override
        protected Object process(Double d1, Long l2, Map<String, Object> param)
        {
            return d1 + l2;
        }

        @Override
        protected Object process(Double d1, Double d2, Map<String, Object> param)
        {
            return d1 + d2;
        }

        @Override
        protected Object process(Double d1, String s2, Map<String, Object> param)
        {
            return d1 + s2;
        }
    }

    public static class MinusOperand extends BasicOperandImpl
    {
        public MinusOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object process(Double d1, Double d2, Map<String, Object> param)
        {
            return d1 - d2;
        }

        @Override
        protected Object process(Double d1, Long l2, Map<String, Object> param)
        {
            return d1 - l2;
        }

        @Override
        protected Object process(Long l1, Double d2, Map<String, Object> param)
        {
            return l1 - d2;
        }

        @Override
        protected Object process(Long l1, Long l2, Map<String, Object> param)
        {
            return l1 - l2;
        }
    }

    public static class TimesOperand extends BasicOperandImpl
    {
        public TimesOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object process(Long l1, Long l2, Map<String, Object> param)
        {
            return l1 * l2;
        }

        @Override
        protected Object process(Long l1, Double d2, Map<String, Object> param)
        {
            return l1 * d2;
        }

        @Override
        protected Object process(Double d1, Long l2, Map<String, Object> param)
        {
            return d1 * l2;
        }

        @Override
        protected Object process(Double d1, Double d2, Map<String, Object> param)
        {
            return d1 * d2;
        }
    }

    public static class DivisionOperand extends BasicOperandImpl
    {
        public DivisionOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object process(Double d1, Double d2, Map<String, Object> param)
        {
            return d1 / d2;
        }

        @Override
        protected Object process(Double d1, Long l2, Map<String, Object> param)
        {
            return d1 / l2;
        }

        @Override
        protected Object process(Long l1, Double d2, Map<String, Object> param)
        {
            return l1 / d2;
        }

        @Override
        protected Object process(Long l1, Long l2, Map<String, Object> param)
        {
            return l1 / l2.doubleValue();
        }
    }

    public static class RemainOperand extends BasicOperandImpl
    {
        public RemainOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object process(Long l1, Long l2, Map<String, Object> param)
        {
            return l1 % l2;
        }

        @Override
        protected Object process(Long l1, Double d2, Map<String, Object> param)
        {
            return l1 % d2;
        }

        @Override
        protected Object process(Double d1, Long l2, Map<String, Object> param)
        {
            return d1 % l2;
        }

        @Override
        protected Object process(Double d1, Double d2, Map<String, Object> param)
        {
            return d1 % d2;
        }
    }

    public static class EqOperand extends BasicOperandImpl
    {
        public EqOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object process(Boolean b1, Boolean b2, Map<String, Object> param)
        {
            return b1.booleanValue() == b2.booleanValue();
        }

        @Override
        protected Object process(Long l1, Long l2, Map<String, Object> param)
        {
            return l1.longValue() == l2.longValue();
        }

        @Override
        protected Object process(Double d1, Long l2, Map<String, Object> param)
        {
            return d1.doubleValue() == l2.longValue();
        }

        @Override
        protected Object process(Long l1, Double d2, Map<String, Object> param)
        {
            return l1.longValue() == d2.doubleValue();
        }

        @Override
        protected Object process(Double d1, Double d2, Map<String, Object> param)
        {
            return d1.doubleValue() == d2.doubleValue();
        }

        @Override
        protected Object process(String s1, String s2, Map<String, Object> param)
        {
            return s2.equals(s1);
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
        public Object calculate(Map<String, Object> param)
        {
            return ((Boolean) eqOperand.calculate(param)) == false;
        }
    }

    public static class GeOperand extends BasicOperandImpl
    {
        public GeOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object process(Double d1, Double d2, Map<String, Object> param)
        {
            return d1 >= d2;
        }

        @Override
        protected Object process(Double d1, Long l2, Map<String, Object> param)
        {
            return d1 >= l2;
        }

        @Override
        protected Object process(Long l1, Double d2, Map<String, Object> param)
        {
            return l1 >= d2;
        }

        @Override
        protected Object process(Long l1, Long l2, Map<String, Object> param)
        {
            return l1 >= l2;
        }
    }

    public static class GtOperand extends BasicOperandImpl
    {
        public GtOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object process(Double d1, Double d2, Map<String, Object> param)
        {
            return d1 > d2;
        }

        @Override
        protected Object process(Double d1, Long l2, Map<String, Object> param)
        {
            return d1 > l2;
        }

        @Override
        protected Object process(Long l1, Double d2, Map<String, Object> param)
        {
            return l1 > d2;
        }

        @Override
        protected Object process(Long l1, Long l2, Map<String, Object> param)
        {
            return l1 > l2;
        }
    }

    public static class LeOperand extends BasicOperandImpl
    {
        public LeOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object process(Double d1, Double d2, Map<String, Object> param)
        {
            return d1 <= d2;
        }

        @Override
        protected Object process(Double d1, Long l2, Map<String, Object> param)
        {
            return d1 <= l2;
        }

        @Override
        protected Object process(Long l1, Double d2, Map<String, Object> param)
        {
            return l1 <= d2;
        }

        @Override
        protected Object process(Long l1, Long l2, Map<String, Object> param)
        {
            return l1 <= l2;
        }
    }

    public static class LtOperand extends BasicOperandImpl
    {
        public LtOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        protected Object process(Double d1, Double d2, Map<String, Object> param)
        {
            return d1 < d2;
        }

        @Override
        protected Object process(Double d1, Long l2, Map<String, Object> param)
        {
            return d1 < l2;
        }

        @Override
        protected Object process(Long l1, Double d2, Map<String, Object> param)
        {
            return l1 < d2;
        }

        @Override
        protected Object process(Long l1, Long l2, Map<String, Object> param)
        {
            return l1 < l2;
        }
    }
}
