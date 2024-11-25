package com.jfirer.jfireel.expression.impl.operand.method.compile;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.jfireel.expression.ELConfig;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.method.MethodInvoker;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class CompileStaticMethod implements Operand
{
    private Operand operand;

    public CompileStaticMethod(Class ckazz, Operand[] argOperands, String memberName, String fragment, ELConfig elConfig)
    {
        operand = new AnalyseOperand(ckazz, argOperands, memberName, fragment, elConfig);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return operand.calculate(contextParam);
    }

    @Override
    public void clearFragment()
    {
        operand.clearFragment();
    }

    @AllArgsConstructor
    class AnalyseOperand implements Operand
    {
        private Class     clazz;
        private Operand[] argOperands;
        private String    memberName;
        private String    fragment;
        private ELConfig  elConfig;

        @SneakyThrows
        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object[] methodParamValues = Arrays.stream(argOperands).map(operand -> operand.calculate(contextParam)).toArray(Object[]::new);
            Method   executable        = (Method) MethodInvoker.findExecutable(clazz, methodParamValues, memberName);
            if (executable == null)
            {
                throw new IllegalArgumentException("解析过程中发现未能发现匹配的方法,方法名为:" + memberName + "。异常解析位置为" + fragment);
            }
            final int[] classIds = Arrays.stream(executable.getParameterTypes()).mapToInt(ReflectUtil::getClassId).toArray();
            Operand     make     = MethodInvoker.make(null, executable, argOperands, classIds, elConfig);
            CompileStaticMethod.this.operand = make;
            return executable.invoke(null, MethodInvoker.compatibleValues(methodParamValues, classIds));
        }

        @Override
        public void clearFragment()
        {
            if (argOperands != null)
            {
                for (Operand each : argOperands)
                {
                    each.clearFragment();
                }
            }
            fragment = null;
        }
    }
}
