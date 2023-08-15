package com.jfirer.jfireel.expression2.impl.operand;

import com.jfirer.jfireel.expression2.Operand;

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

    @Override
    public Object calculate(Map<String, Object> param)
    {
        Object leftValue  = left.calculate(param);
        Object rightValue = right.calculate(param);
        if (leftValue instanceof String s1)
        {
            if (rightValue instanceof String s2)
            {
                return process(s1, s2, param);
            }
            else if (rightValue instanceof Long l2)
            {
                return process(s1, l2, param);
            }
            else if (rightValue instanceof Double d2)
            {
                return process(s1, d2, param);
            }
            else
            {
                throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
            }
        }
        else if (leftValue instanceof Long l1)
        {
            if (rightValue instanceof String s2)
            {
                return process(l1, s2, param);
            }
            else if (rightValue instanceof Long l2)
            {
                return process(l1, l2, param);
            }
            else if (rightValue instanceof Double d2)
            {
                return process(l1, d2, param);
            }
            else
            {
                throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
            }
        }
        else if (leftValue instanceof Double d1)
        {
            if (rightValue instanceof String s2)
            {
                return process(d1, s2, param);
            }
            else if (rightValue instanceof Long l2)
            {
                return process(d1, l2, param);
            }
            else if (rightValue instanceof Double d2)
            {
                return process(d1, d2, param);
            }
            else
            {
                throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
            }
        }
        else
        {
            throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
        }
    }

    protected Object process(Double d1, Double d2, Map<String, Object> param) {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Double d1, Long l2, Map<String, Object> param)   {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Double d1, String s2, Map<String, Object> param) {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Long l1, Double d2, Map<String, Object> param)   {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Long l1, Long l2, Map<String, Object> param)     {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(Long l1, String s2, Map<String, Object> param)   {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(String s1, Double d2, Map<String, Object> param) {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(String s1, Long l2, Map<String, Object> param)   {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    protected Object process(String s1, String s2, Map<String, Object> param) {throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);}

    public static class AndOperand extends BasicOperandImpl
    {
        public AndOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
        }

        @Override
        public Object calculate(Map<String, Object> param)
        {
            Object leftValue  = left.calculate(param);
            Object rightValue = right.calculate(param);
            if (leftValue instanceof Boolean b1 && rightValue instanceof Boolean b2)
            {
                return b1.booleanValue() && b2.booleanValue();
            }
            else
            {
                throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
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
        public Object calculate(Map<String, Object> param)
        {
            Object leftValue  = left.calculate(param);
            Object rightValue = right.calculate(param);
            if (leftValue instanceof Boolean b1 && rightValue instanceof Boolean b2)
            {
                return b1.booleanValue() || b2.booleanValue();
            }
            else
            {
                throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
            }
        }
    }

    public static class PlusOperand extends BasicOperandImpl
    {
        public PlusOperand(Operand left, Operand right, String fragment)
        {
            super(left, right, fragment);
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

    public static class Mutlioperand extends BasicOperandImpl
    {
        public Mutlioperand(Operand left, Operand right, String fragment)
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
        public Object calculate(Map<String, Object> param)
        {
            Object leftValue  = left.calculate(param);
            Object rightValue = right.calculate(param);
            if (leftValue instanceof Boolean b1 && rightValue instanceof Boolean b2)
            {
                return b1.booleanValue() == b2.booleanValue();
            }
            else if (leftValue instanceof String s1 && rightValue instanceof String s2)
            {
                return s1.equals(s2);
            }
            else if (leftValue instanceof Long l1)
            {
                if (rightValue instanceof Long l2)
                {
                    return l1.longValue() == l2.longValue();
                }
                else if (rightValue instanceof Double d2)
                {
                    return l1.longValue() == d2.doubleValue();
                }
                else
                {
                    throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
                }
            }
            else if (leftValue instanceof Double d1)
            {
                if (rightValue instanceof Long l2)
                {
                    return d1.doubleValue() == l2.longValue();
                }
                else if (rightValue instanceof Double d2)
                {
                    return d1.doubleValue() == d2.doubleValue();
                }
                else
                {
                    throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
                }
            }
            else
            {
                throw new IllegalArgumentException("操作数解析出现异常，该操作符能够识别的操作数类型只有字符串，Long，Double。异常解析位置为" + fragment);
            }
        }
    }
}
